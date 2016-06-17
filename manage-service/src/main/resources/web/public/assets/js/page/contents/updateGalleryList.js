/**
 * Created by chendeyu on 2016/3/17.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var ownerId =commonUtil.getQuery("ownerId");
    var layer=require("layer");
    exports.fromValidata=function() {
        $("#updateGalleryListForm").validate({
            rules: {
                txtOrderWeight: {
                    required: true,
                    digits: true,
                }
            },
            messages: {
                widgetTypeId: {
                    digits: "请填正确的数字",
                }
            },
            submitHandler: function (form, ev) {
                commonUtil.setDisabled("jq-cms-Save");
                var thumbUri = $("#thumbUri").val();
                if(thumbUri==null||thumbUri==""){
                    layer.msg("请先上传图片", {
                        icon: 2,
                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                    }, function () {
                        commonUtil.cancelDisabled("jq-cms-Save");
                    });
                }
                else{
                    $.ajax({
                        url: "/manage/gallery/saveGalleryList",
                        data: {
                            id: $("#id").val(),
                            galleryId: $("#galleryId").val(),
                            ownerId: ownerId,
                            thumbUri: $("#thumbUri").val(),
                            wide: $("#wide").val(),
                            height: $("#height").val(),
                            orderWeight: $("#txtOrderWeight").val()
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
                                    });
                                }
                                if (index == 500)
                                    layer.msg("操作失败", {
                                        icon: 2,
                                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                    }, function () {
                                        $("#txtOrderWeight").val("50");
                                        $("#uploadThumbUri").attr("src", null);
                                        $("#thumbUri").val("");

                                        commonUtil.cancelDisabled("jq-cms-Save");
                                    });
                            }

                        },
                        error: function () {
                            commonUtil.cancelDisabled("jq-cms-Save");
                        }
                    });}
                return false;
            },
            invalidHandler: function () {
                return true;
            }
        });
    }

    exports.uploadImg=function(){
        uploadModule.uploadImg();
    }

    //上传图片模块
    var uploadModule= {
        uploadImg: function () {
            $("#btnFile").jacksonUpload({
                url: "/manage/cms/imgUpLoad",
                name: "btnFile",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                data: {
                    ownerId: ownerId
                },
                callback: function (json) {
                    if (json != null) {
                        var code = parseInt(json.code);
                        switch (code) {
                            case 200:
                                $("#uploadThumbUri").attr("src", json.data.fileUrl);
                                $("#thumbUri").val(json.data.fileUri);
                                $("#wide").val(json.data.wide);
                                $("#height").val(json.data.height);
                                layer.msg("操作成功", {
                                    icon: 1,
                                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                }, function () {
                                    commonUtil.cancelDisabled("jq-cms-Save");
                                });
                                break;
                            case 403:
                                layer.msg("文件格式错误", {
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
        },
    }



});

