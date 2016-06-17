/**
 * Created by Administrator on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addNoticeForm").validate({
        rules: {
            title:{
                required: true,
            },
            content:{
                required: true,
            },
            OrderWeight:{
                digits:true,
            }
        },
        messages: {
            title:{
                required:"请输入标题名称"
            },
            content:{
                required:"请输入公告内容",
            },
            OrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var ownerId =commonUtil.getQuery("ownerId");
            $.ajax({
                url: "/notice/saveNotice",
                data: {
                    id:$("#hidNoticeID").val(),
                    title:$("#title").val(),
                    description:$("#description").val(),
                    ownerId:ownerId,
                    content: $("#content").val(),
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
                            layer.msg("保存成功！", {time: 2000})
                            $("#title").val("");
                            $("#description").val("");
                            $("#content").val("");
                            $("#orderWeight").val("50")
                            //layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000})
                            //setTimeout(function(){
                            //        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&ownerId="+ownerId;
                            //    }
                            //    ,1000);
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