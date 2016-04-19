/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var customerId = commonUtil.getQuery("customerId");
    $("#addSiteForm").validate({
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
            domains: {
                required: true
                //remote: {
                //    url: "/site/isExistsDomain",     //后台处理程序
                //    type: "post",               //数据发送方式
                //    dataType: "json",           //接受数据格式
                //    data: {                     //要传递的数据
                //        domains: function () {
                //            return $("#domains").val();
                //        }
                //    }
                //},
            },
            homeDomain: {
                required: true,
            },
            copyright: {
                required: true,
            },
            regionId: {
                selrequired: "-1"
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
            domains: {
                required: "请输入域名"
                //remote:"存在相同的域名"
            },
            homeDomain: {
                required: "必须存在一个主推域名"
            },
            description: {
                maxlength: "站点描述不能超过200个字符"
            },
            keywords: {
                maxlength: "站点关键字不能超过200个字符"
            },
            regionId: {
                selrequired: "请选择地区"
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var customTemplateUrl = $("#customTemplateUrl").val();
            var layer = require("layer");
            var custom = $("#custom_0").val();
            var ary = $("#domains").val();
            var nary = ary.split(",");
            var flag = 0;
            var isPersonalise;
            var x = $('input:radio[id="isPersonalise_1"]:checked').val();//判断是否个性化
            if (x == undefined) {
                isPersonalise = false;
            }
            else {
                isPersonalise = true;
            }
            for (var i = 0; i < nary.length - 1; i++) {
                if (nary[i] == nary[i + 1]) {
                    flag = 1;
                    layer.msg("操作失败,请不要填写重复域名", {time: 2000})
                    break;
                }
            }
            if (flag == 0) {
                if ($("#custom_0").is(':checked')) {
                    layer.msg("请填上根路径", {time: 2000})
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
                else {
                    $.ajax({
                        url: "/site/saveSite",
                        data: {
                            siteId: $("#hidSiteID").val(),
                            customerId: customerId,
                            name: $("#name").val(),
                            title: $("#title").val(),
                            keywords: $("#keywords").val(),
                            copyright: $("#copyright").val(),
                            resourceUrl: $("#resourceUrl").val(),
                            logoUri: $("#logoUri").val(),
                            custom: $("#custom_0").val(),
                            personalise: isPersonalise,
                            customTemplateUrl: $("#customTemplateUrl").val(),
                            domains: $("#domains").val(),
                            homeDomains: $("#homeDomain").val(),
                            regionId: $("#regionId").val(),
                            description: $("#description").val(),
                            siteType: $("#siteType").val()
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
                                    $("#name").val("");
                                    $("#title").val("");
                                    $("#keywords").val("");
                                    $("#copyright").val("");
                                    $("#resourceUrl").val("");
                                    $("#logoUri").val("");
                                    $("#custom_0").val("1");
                                    $("#homeDomain").val(""),
                                        $("#uploadLogoUri").attr("src", "");
                                    document.getElementById('custom_0').checked = true;
                                    document.getElementById("cUrl").style.display = "";
                                    $("#customTemplateUrl").val("");
                                    $("#domains").val("");
                                    $("#regionId").val("-1");
                                    $("#description").val("");
                                }
                                if (index == 500) {
                                    layer.msg("操作失败", {time: 2000})
                                }
                                if (index == 203) {
                                    layer.msg("域名已被占用，请修改域名", {time: 2000})
                                }
                                if (index == 406) {
                                    layer.msg("请添加主推域名", {time: 2000})
                                }
                            }
                            commonUtil.cancelDisabled("jq-cms-Save");
                        },
                        error: function () {
                            commonUtil.cancelDisabled("jq-cms-Save");
                        }
                    })
                }
            }
            return false;
        },
        invalidHandler: function () {
            return true;
        },
    });

    function selectPersoines() {
        var obj = $(".js-select-personalise");
        $(".js-custome-template").hide();
        $.each(obj, function (item, dom) {
            $(dom).change(function () {
                var id = $(dom).attr("id");
                if ($(dom).is(':checked')) {
                    if (id == "isPersonalise_1") {
                        $(".js-custome-template").hide();
                    } else {
                        $(".js-custome-template").show();
                        if ($("#custom_0").is(':checked')) {
                            changeradio(1)
                        } else {
                            changeradio(2);
                        }
                    }
                }
            })
        })
    }

    function selectTemplateType() {
        var obj = $(".js-template");
        $.each(obj, function (item, dom) {
            $(dom).click(function () {
                var type = $(dom).data('type');
                changeradio(type);
            });
        })
    }

    function changeradio(t) {
        if (t == 1) {//选择是
            $("#customTemplateUrl").val("");
            $("#custom_0").val("1");
            document.getElementById("cUrl").style.display = "";
        }
        else {//选择否
            $("#custom_0").val("0");
            document.getElementById("cUrl").style.display = "none";
        }

    }

    selectPersoines();
    selectTemplateType();

    //$("#btnFile").bind("change",function(){
    //    var btnFile=document.getElementById('btnFile').getAttribute("id");
    //    uploadImg(btnFile);
    //})
    //
    //function uploadImg (btnFile) {
    //    layer.msg("正在上传", {time: 2000});
    //    var commonUtil = require("common");
    //    commonUtil.setDisabled("jq-cms-Save");
    //    var customerId =commonUtil.getQuery("customerId");
    //    $.ajaxFileUpload({
    //        url: "/cms/siteUpLoad",
    //        secureuri: false,//安全协议
    //        fileElementId: btnFile,//id
    //        dataType: 'json',
    //        type: "post",
    //        data:{
    //            customerId: customerId
    //        },
    //        error: function (data, status, e) {
    //
    //        },
    //        success: function (json) {
    //        if (json.result == 1) {
    //            $("#uploadLogoUri").attr("src", json.fileUrl);
    //            $("#logoUri").val(json.fileUri);
    //            commonUtil.cancelDisabled("jq-cms-Save");
    //            layer.msg("操作成功", {time: 2000});
    //        } else {
    //            layer.msg("操作失败", {time: 2000});
    //            commonUtil.cancelDisabled("jq-cms-Save");
    //        }
    //    }
    //    });
    //}


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



