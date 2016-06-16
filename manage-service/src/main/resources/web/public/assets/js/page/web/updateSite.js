/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#regionId").attr("disabled", "disabled");
    var commonUtil = require("common");
    var customerId = commonUtil.getQuery("customerId");
    $("#updateSiteForm").validate({
        rules: {
            name: {
                required: true,
            },
            title: {
                required: true,
            },
            description: {
                maxlength: 200
            },
            keywords: {
                maxlength: 200
            },
            copyright: {
                required: true,
            }
        },
        messages: {
            name: {
                required: "名称为必输项"
            },
            copyright: {
                required: "版权信息为必输项"
            },
            title: {
                required: "标题为必输项"
            },
            description: {
                maxlength: "站点描述不能超过200个字符"
            },
            keywords: {
                maxlength: "站点关键字不能超过200个字符"
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var layer = require("layer");
            $.ajax({
                url: "/site/saveSite",
                data: {
                    siteId: $("#hidSiteID").val(),
                    customerId: customerId,
                    name: $("#name").val(),
                    title: $("#title").val(),
                    keywords: $("#keywords").val(),
                    copyright: $("#copyright").val(),
                    logoUri: $("#logoUri").val(),
                    description: $("#description").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer = require("layer");
                    if (data != null) {
                        var index = parseInt(data.code);
                        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                        if (index == 200) {
                            var layer = require("layer");
                            layer.msg("修改成功,2秒后将自动返回列表页面", {time: 2000})
                            parent.layer.close(layerIndex);
                        }
                        else if(index==404){
                            layer.msg("更新失败,站点信息查找失败", {time: 2000})
                        }
                        else if (index == 500){
                            layer.msg("修改失败", {time: 2000})
                        }
                    }
                    commonUtil.cancelDisabled("jq-cms-Save");
                },
                error: function () {
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
            })
            return false;
        },
        invalidHandler: function () {
            return true;
        },

    });
    //
    //selectPersoines();
    //selectTemplateType();

    //上传图片模块
    var uploadModule = {
        uploadImg: function () {
            $("#btnFile").jacksonUpload({
                url: "/cms/siteUpLoad",
                name: "btnFile",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                boxWidth:508,
                data: {
                    customerId: customerId
                },
                callback: function (json) {
                    if (json != null) {
                        var code = parseInt(json.code);
                        switch (code) {
                            case 200:
                                $("#uploadLogoUri").attr("src", json.data.fileUrl);
                                $("#logoUri").val(json.data.fileUri);
                                commonUtil.cancelDisabled("jq-cms-Save");
                                layer.msg("操作成功", {time: 2000});
                                break;
                            case 403:
                                layer.msg("文件格式错误,请上传jpg, jpeg,png,gif,bmp格式的图片", {time: 2000});
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

    uploadModule.uploadImg();
});