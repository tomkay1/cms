/**
 * Created by chendeyu on 2016/3/17.
 */
define(function (require, exports, module) {
    $("#addWidgetTypeForm").validate({
        rules: {
            name:{
                required: true,
            },
            txtOrderWeight:{
                required: true,
                digits:true,
            }
        },
        messages: {
            txtOrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/widget/saveWidgetType",
                data: {
                    id:$("#hidWidgetTypeID").val(),
                    name: $("#name").val(),
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
                            var layer=require("layer");
                            layer.msg("操作成功", {
                                icon: 1,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function(){
                                //$("#name").val("");
                                //$("#txtOrderWeight").val("50");
                                location.reload();
                            });
                        }
                        if(index==500) {
                            layer.msg("操作失败", {
                                icon: 2,
                                time: 2000 //2秒关闭（如果不配置，默认是3秒）
                            }, function () {
                                $("#name").val("");
                                $("#txtOrderWeight").val("50");
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