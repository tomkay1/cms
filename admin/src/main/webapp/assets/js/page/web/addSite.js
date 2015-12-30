/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addSiteForm").validate({
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
                            layer.msg("操作成功",{time: 2000});
                            $("#name").val("");
                            $("#title").val("");
                            $("#keywords").val("");
                            $("#copyright").val("");
                            $("#custom_0").val("");
                            document.getElementById('custom_0').checked = true;
                            document.getElementById("cUrl").style.visibility="visible";
                            $("#customTemplateUrl").val("");
                            $("#domains").val("");
                            $("#regionId").val("-1");
                            $("#description").val("");

                        }
                        if(index==500){layer.msg("操作失败",{time: 2000})}

                        if(index==999){
                            layer.msg("域名已被占用",{time: 2000})
                        }
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
        haha : function(){
            alert("!");
        }
    });
});
