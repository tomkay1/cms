/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#updateLinkForm").validate({
        //rules: {
        //    title:{
        //        required: true,
        //        maxlenght:50
        //    }
        //},
        //messages: {
        //    title:{
        //        required:"链接标题为必填项",
        //        maxlenght:"链接标题最大为50个字符"
        //    }
        //},
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var customerId =commonUtil.getQuery("customerId");
            $.ajax({
                url: "/link/saveLink",
                data: {
                    id:$("#hidLinkID").val(),
                    title:$("#title").val(),
                    customerId:customerId,
                    linkUrl: $("#linkUrl").val(),
                    description: $("#description").val(),
                    categoryId: $("#categoryId").val(),
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
                            layer.msg("修改成功,2秒后将自动返回列表页面",{time: 2000})
                            commonUtil.cancelDisabled("jq-cms-Save");
                            window.location.href="http://"+window.location.host+"/"+"link/linkList?customerid=3447";
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