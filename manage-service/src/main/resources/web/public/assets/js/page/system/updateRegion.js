/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#updateRegionForm").validate({
        rules: {
            regionCode:{
                required: true,
            },
            regionName:{
                required: true,
            },
            langName:{
                required: true,
            },
            langCode:{
                required: true,
            },
            txtModelType: {
                selrequired: "-1"
            },
            txtOrderWeight:{
                digits:true,
            }
        },
        messages: {
            regionCode:{
                required:"地区编号必填项"
            },
            regionName:{
                required:"地区名必填项"
            },
            langName:{
                required:"语言名必填项"
            },
            langCode:{
                required:"语言编号必填项"
            },
            txtModelDescription:{
                maxlength:"模型描述不能超过200个字符"
            },
            txtModelType: {
                selrequired: "请选择模型类型"
            },
            txtOrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/manage/region/saveRegion",
                data: {
                    id:$("#hidRegionID").val(),
                    regionCode: $("#regionCode").val(),
                    regionName: $("#regionName").val(),
                    langCode: $("#langCode").val(),
                    langTag: $("#langTag").val(),
                    langName: $("#langName").val()
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
                            layer.msg("修改成功，即将返回列表", {
                                icon: 1,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                window.location.href="http://"+window.location.host+"/"+"region/regionList";
                            });

                        }
                        if(index==500){

                            layer.msg("操作失败", {
                                icon: 2,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                commonUtil.cancelDisabled("jq-cms-Save");
                            });

                        }
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
});