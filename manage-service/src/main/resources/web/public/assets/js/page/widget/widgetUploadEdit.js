/**
 * Created by chendeyu on 2016/3/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var ownerId =commonUtil.getQuery("ownerId");
    var layer=require("layer");
    exports.fromValidata=function() {
        $("#widgetUploadForm").validate({
            rules: {
                content: {
                    required: true,
                },
            },
            messages: {
                //widgetTypeId: {
                //    selrequired: "请选择类型",
                //}
            },
            submitHandler: function (form, ev) {
                var commonUtil = require("common");
                commonUtil.setDisabled("jq-cms-Save");
                    $.ajax({
                        url: "/manage/cms/saveWidgetEdit",
                        data: {
                            id: $("#id").val(),
                            path: $("#resourceUri").val(),
                            content: $("#content").val(),
                        },
                        type: "POST",
                        dataType: 'json',
                        success: function (data) {
                            var layer = require("layer");
                            if (data != null) {
                                var index = parseInt(data.code);
                                if (index == 200) {
                                    var layer = require("layer");
                                    layer.msg("操作成功", {
                                        icon: 1,
                                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                    }, function () {
                                        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                        parent.layer.close(layerIndex);
                                        commonUtil.cancelDisabled("jq-cms-Save");
                                    });
                                }
                                else{
                                    layer.msg("操作失败", {
                                    icon: 2,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                }, function () {
                                        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                        parent.layer.close(layerIndex);
                                        commonUtil.cancelDisabled("jq-cms-Save");
                                });}

                            }

                        },
                        error: function () {
                            commonUtil.cancelDisabled("jq-cms-Save");
                        }
                    });
                return false;
            },
            invalidHandler: function () {
                return true;
            }
        });
    }

    exports.uploadImg=function(){
        uploadModule.uploadWidget();
    }

    var uploadModule={
    uploadWidget:function(){
        var id = $("#id").val();
        $("#btnFile1").jacksonUpload({
            url: "/manage/cms/widgetUpLoadEdit?id="+id,
            name: "btnFile1",
            enctype: "multipart/form-data",
            submit: true,
            method: "post",
            data:{
                //id: hidWidgetTypeID
            },
            callback: function (json) {
                if(json!=null)
                {
                    var code=parseInt(json.code);
                    switch (code){
                        case 200:
                            $("#content").val(json.data.fileContent);
                            $("#resourceUri").val(json.data.fileUrl);
                            layer.msg("操作成功", {
                                icon: 1,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function () {
                                commonUtil.cancelDisabled("jq-cms-Save");
                            });
                            break;
                        case 403:
                            layer.msg("文件格式错误,请上传.html格式的文件", {
                                icon: 2,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            })
                            break;
                        case 502:
                            layer.msg("服务器错误,请稍后再试", {time: 2000});
                            break;
                    }
                }
            },
            timeout: 30000,
            timeout_callback: function () {
                layer.msg("图片上传操作", {time: 2000});
            }
        });
    }

}

});


