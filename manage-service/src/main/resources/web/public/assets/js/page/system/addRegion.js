/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#addRegionForm").validate({
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
                required: true,
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
                    langName: $("#langName").val(),
                    orderWeight: $("#txtOrderWeight").val()
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
                            layer.msg("操作成功", {
                                icon: 1,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                $("#regionCode").val("");
                                $("#regionName").val("");
                                $("#langCode").val("");
                                $("#langTag").val("");
                                $("#langName").val("");
                                commonUtil.cancelDisabled("jq-cms-Save");
                            });

                        }
                        if(index==500)
                            layer.msg("操作失败", {
                                icon: 2,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                commonUtil.cancelDisabled("jq-cms-Save");
                            });
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