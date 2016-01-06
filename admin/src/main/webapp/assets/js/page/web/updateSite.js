/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#updateSiteForm").validate({
        //rules: {
        //    txtModelName:{
        //        required: true,
        //    },
        //    txtModelDescription:{
        //        maxlength:200
        //    },
        //    txtModelType: {
        //        selrequired: "-1"
        //    },
        //    txtOrderWeight:{
        //        digits:true,
        //    }
        //},
        //messages: {
        //    txtModelName:{
        //        required:"模型名称为必输项"
        //    },
        //    txtModelDescription:{
        //        maxlength:"模型描述不能超过200个字符"
        //    },
        //    txtModelType: {
        //        selrequired: "请选择模型类型"
        //    },
        //    txtOrderWeight:{
        //        digits:"请输入数字",
        //    }
        //},
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var customerId =commonUtil.getQuery("customerId");
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
            });
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
