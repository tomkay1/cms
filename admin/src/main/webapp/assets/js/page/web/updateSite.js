/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#updateSiteForm").validate({
        rules: {
            name:{
                required: true,
            },
            title:{
                required: true,
            },
            description:{
                maxlength:200
            },
            domains:{
                required: true,
            },
            copyright:{
                required: true,
            },
            txtModelDescription:{
                maxlength:200
            },
            regionId: {
                selrequired: "-1"
            },
            txtOrderWeight:{
                digits:true,
            }
        },
        messages: {
            name:{
                required:"名称为必输项"
            },
            copyright:{
                required:"版权信息为必输项"
            },
            title:{
                required:"标题为必输项"
            },
            domains:{
                required:"域名为必输项"
            },
            txtModelDescription:{
                maxlength:"站点描述不能超过200个字符"
            },
            regionId: {
                selrequired: "地区"
            },
            txtOrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var customerId =commonUtil.getQuery("customerId");
            var custom= $("#custom_0").val();
            var customTemplateUrl= $("#customTemplateUrl").val();
            var f=$("#logoUri").val();
            if(f==""){
                layer.msg("请上传图片",{time: 2000});commonUtil.cancelDisabled("jq-cms-Save");
            }
            else if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f)) {
                layer.msg("请上传正确图片",{time: 2000});commonUtil.cancelDisabled("jq-cms-Save");
            }
            else{
            if(custom==1&&(customTemplateUrl==""||customTemplateUrl==null)){
                layer.msg("请填上根路径",{time: 2000})
                commonUtil.cancelDisabled("jq-cms-Save");
            }
            else{
            $.ajax({
                url: "/site/saveSite",
                data: {
                    siteId:$("#hidSiteID").val(),
                    customerId:customerId,
                    name: $("#name").val(),
                    title: $("#title").val(),
                    keywords: $("#keywords").val(),
                    copyright: $("#copyright").val(),
                    custom: $("#custom_0").val(),
                    logoUri: $("#logoUri").val(),
                    customTemplateUrl: $("#customTemplateUrl").val(),
                    domains: $("#domains").val(),
                    regionId: $("#regionId").val(),
                    description: $("#description").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer=require("layer");
                    if(data!=null)
                    {
                        var index=parseInt(data.code);
                        if(index==200)
                        {
                            var layer=require("layer");
                            layer.msg("修改成功,2秒后将自动返回列表页面",{time: 2000})
                            commonUtil.cancelDisabled("jq-cms-Save");
                            window.location.href="http://"+window.location.host+"/"+"site/siteList?customerId="+customerId;
                            //commonUtil.redirectUrl("/model/modelList");
                            //$("#txtModelName").val("");
                            //$("#txtModelDescription").val("");
                        }
                        if(index==500)
                            layer.msg("修改失败",{time: 2000})
                    }
                    commonUtil.cancelDisabled("jq-cms-Save");
                },
                error: function () {
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
            })}};
            return false;
        },
        invalidHandler: function () {
            return true;
        },

    });
});
function changeradio(t){
    if(t==1){//选择是
        $("#customTemplateUrl").val("");
        $("#custom_0").val("1");
        document.getElementById("cUrl").style.display="";
    }
    else{//选择否
        $("#custom_0").val("0");
        document.getElementById("cUrl").style.display="none";
    }

}

function uploadImg (btnFile, showImgId, pathId) {
    layer.msg("正在上传", {time: 2000});
    $.ajaxFileUpload({
        url: "/cms/SiteUpload",
        secureuri: false,//安全协议
        fileElementId: btnFile,//id
        dataType: 'json',
        type: "post",
        data: null,
        error: function (data, status, e) {

        },
        success: function (json) {
            if (json.result == 1) {
                $("#" + showImgId).attr("src", json.fileUrl);
                $("#" + pathId).val(json.fileUri);
                layer.msg("操作成功", {time: 2000});
            } else {
                layer.msg("操作失败", {time: 2000});
            }
        }
    });
}
