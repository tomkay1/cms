/**
 * Created by Administrator on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addModelForm").validate({
        rules: {
            txtModelName:{
                required: true,
            },
            txtModelDescription:{
                maxlength:200
            },
            txtModelType: {
                selrequired: "-1"
            },
            txtOrderWeight:{
                digits:true,
            }
        },
        messages: {
            txtModelName:{
              required:"模型名称为必输项"
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
                url: "/model/saveModel",
                data: {
                    id:$("#hidModelID").val(),
                    name: $("#txtModelName").val(),
                    description: $("#txtModelDescription").val(),
                    type: $("#txtModelType").val(),
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
                            layer.msg("操作成功",{time: 2000});
                            $("#txtModelName").val("");
                            $("#txtModelDescription").val("50");
                        }
                        if(index==500)
                            layer.msg("操作失败",{time: 2000})
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
        }
    });
});
