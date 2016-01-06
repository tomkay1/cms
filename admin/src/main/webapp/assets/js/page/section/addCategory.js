/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#addColumnForm").validate({
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
            $.ajax({
                url: "/category/saveCategory",
                data: {
                    id:$("#hidRegionID").val(),
                    siteId:$("#siteId").val(),
                    parentId: $("#parentId").val(),
                    name: $("#name").val(),
                    model: $("#modelType").val(),
                    orderWeight: $("#orderWeight").val()
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
                            $("#modelType").val("");
                            $("#orderWeight").val("50");
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