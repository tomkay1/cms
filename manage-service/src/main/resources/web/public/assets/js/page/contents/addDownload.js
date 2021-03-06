/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var ownerId =commonUtil.getQuery("ownerId");
    exports.fromValidata=function() {
        $("#addDownloadForm").validate({
            rules: {
                title: {
                    required: true,
                },
                downloadUrl: {
                    required: true,
                },
                categoryId: {
                    selrequired: "-1"
                },
                OrderWeight: {
                    digits: true,
                }
            },
            messages: {
                title: {
                    required: "请输入文件名称"
                },
                downloadUrl: {
                    required: "请输入下载地址"
                },
                categoryId: {
                    selrequired: "请选择栏目"
                },
                OrderWeight: {
                    digits: "请输入数字",
                }
            },
            submitHandler: function (form, ev) {
                var commonUtil = require("common");
                commonUtil.setDisabled("jq-cms-Save");
                var ownerId = commonUtil.getQuery("ownerId");
                $.ajax({
                    url: "/manage/download/saveDownload",
                    data: {
                        id: $("#hidDownloadID").val(),
                        title: $("#title").val(),
                        ownerId: ownerId,
                        downloadUrl: $("#downloadUrl").val(),
                        description: $("#description").val(),
                        categoryId: $("#categoryId").val(),
                        orderWeight: $("#orderWeight").val()
                    },
                    type: "POST",
                    dataType: 'json',
                    success: function (data) {
                        var layer = require("layer");
                        if (data != null) {
                            var index = parseInt(data.code);
                            if (index == 200) {
                                var layer = require("layer");
                                layer.msg("保存成功！", {time: 2000})
                                $("#title").val("");
                                $("#downloadUrl").val("");
                                $("#description").val("");
                                $("#orderWeight").val("50")
                                $("#downloadFile").attr("href", "");
                                $("#downloadFile").css('display','');
                                //layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000})
                                //setTimeout(function(){
                                //        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&ownerId="+ownerId;
                                //    }
                                //    ,1000);
                            }
                            if (index == 500)
                                layer.msg("操作失败", {time: 2000})
                        }
                        commonUtil.cancelDisabled("jq-cms-Save");
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

    exports.uploadDownload=function(){
        uploadModule.uploadDownload();
    }

    //上传文件模块
    var uploadModule={
            uploadDownload:function () {
                $("#btnFile").jacksonUpload({
                    url: "/manage/cms/downloadUpLoad",
                    name: "btnFile",
                    enctype: "multipart/form-data",
                    submit: true,
                    text: "上传文件",
                    method: "post",
                    data: {
                        ownerId: ownerId
                    },
                    callback: function (json) {
                        if (json != null) {
                            var code = parseInt(json.code);
                            switch (code) {
                                case 200:
                                    $("#downloadFile").attr("href", json.data.fileUrl);
                                    $("#downloadFile").css('display','block');
                                    $("#downloadUrl").val(json.data.fileUri);
                                    commonUtil.cancelDisabled("jq-cms-Save");
                                    layer.msg("操作成功", {time: 2000});
                                    break;
                                case 403:
                                    layer.msg("文件格式错误,请上传txt,zip,jar,docx,doc,xlsx格式的文件", {time: 2000});
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



