/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var customerId = commonUtil.getQuery("customerId");
    $("#addSiteForm").validate({
        rules: {
            tempName: {
                required: true,
            },
        },
        messages: {
            name: {
                required: "名称为必输项"
            },
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var layer = require("layer");
            $.ajax({
                url: "/supper/saveTemplate",
                data: {
                    siteId: $("#hidSiteID").val(),
                    customerId: customerId,
                    tempName: $("#tempName").val(),
                    thumbUri: $("#thumbUri").val(),
                    siteId:$("#siteId").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer = require("layer");
                    if (data != null) {
                        var index = parseInt(data.code);
                        if (index == 200) {
                            var layer = require("layer");
                            layer.msg("操作成功", {time: 2000});
                            $("#tempName").val("");
                            $("#thumbUri").val("");
                            $("#siteId").val("-1");
                        }
                        if (index == 500) {
                            layer.msg("操作失败", {time: 2000})
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
    //上传图片模块
    var uploadModule = {
        uploadImg: function () {
            $("#btnFile").jacksonUpload({
                url: "/cms/siteUpLoad",
                name: "btnFile",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                data: {
                    customerId: customerId
                },
                callback: function (json) {
                    if (json != null) {
                        var code = parseInt(json.code);
                        switch (code) {
                            case 200:
                                $("#uploadThumbUri").attr("src", json.data.fileUrl);
                                $("#thumbUri").val(json.data.fileUri);
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



