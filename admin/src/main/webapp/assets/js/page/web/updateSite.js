/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#regionId").attr("disabled","disabled");
    var commonUtil = require("common");
    var customerId =commonUtil.getQuery("customerId");
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
            keywords:{
                maxlength:200
            },
            domains:{
                required: true
                //remote: {
                //    url: "/site/isNoExistsDomain",     //后台处理程序
                //    type: "post",               //数据发送方式
                //    dataType: "json",           //接受数据格式
                //    data: {                     //要传递的数据
                //        siteId:$("#hidSiteID").val(),
                //        domains: function () {
                //            return $("#domains").val();
                //        }
                //    }
                //},
            },
            copyright:{
                required: true,
            },
            regionId: {
                selrequired: "-1"
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
                required:"域名为必输项",
                remote:"域名列表存在重复"
            },
            description:{
                maxlength:"站点描述不能超过200个字符"
            },
            keywords:{
                maxlength:"站点关键字不能超过200个字符"
            },
            regionId: {
                selrequired: "请选择地区"
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var custom= $("#custom_0").val();
            var customTemplateUrl= $("#customTemplateUrl").val();
            var layer=require("layer");
            var custom= $("#custom_0").val();
            var ary = $("#domains").val();
            var nary= ary.split(",");
            var isPersonalise;
            var  x = $('input:radio[id="isPersonalise_1"]:checked').val();//判断是否个性化
            if(x == undefined){
                isPersonalise = false;
            }
            else{
                isPersonalise = true;
            }
            var flag=0;
            for(var i=0;i<nary.length-1;i++)
            {
                if (nary[i]==nary[i+1])
                {
                    flag=1;
                    layer.msg("操作失败,请不要填写重复域名",{time: 2000})
                    break;
                }
            }
            if(flag==0){
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
                    resourceUrl: $("#resourceUrl").val(),
                    copyright: $("#copyright").val(),
                    custom: $("#custom_0").val(),
                    personalise:isPersonalise,
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
                            setTimeout(function(){
                                    window.location.href="http://"+window.location.host+"/"+"site/siteList?customerId="+customerId;
                                }
                                ,1000);
                            //commonUtil.redirectUrl("/model/modelList");
                            //$("#txtModelName").val("");
                            //$("#txtModelDescription").val("");
                        }
                        if(index==500)
                            layer.msg("修改失败",{time: 2000})
                        if(index==203){
                            layer.msg("域名已被占用，请修改域名",{time: 2000})
                        }
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

    //上传图片模块
    var uploadModule={
        uploadImg:function(){
            $("#btnFile").jacksonUpload({
                url: "/cms/siteUpLoad",
                name: "btnFile",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                data:{
                    customerId: customerId
                },
                callback: function (json) {
                    if(json!=null)
                    {
                        var code=parseInt(json.code);
                        switch (code){
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
