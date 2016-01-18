/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#updateNoticeForm").validate({
        rules: {
            title:{
                required: true,
            },
            description:{
                required: true,
            },
            content:{
                required: true,
            },
            categoryId: {
                selrequired: "-1"
            },
            OrderWeight:{
                digits:true,
            }
        },
        messages: {
            title:{
                required:"请输入标题名称"
            },
            description:{
                required: "请输入描述"
            },
            content:{
                required:"请输入公告内容"
            },
            categoryId: {
                selrequired: "请选择栏目"
            },
            OrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var customerId =commonUtil.getQuery("customerId");
            $.ajax({
                url: "/notice/saveNotice",
                data: {
                    id:$("#hidNoticeID").val(),
                    title:$("#title").val(),
                    description:$("#description").val(),
                    customerId:customerId,
                    content: $("#content").val(),
                    categoryId: $("#categoryId").val(),
                    orderWeight: $("#orderWeight").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer=require("layer");
                    var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    if(data!=null)
                    {
                        var index=parseInt(data.code);
                        if(index==200)
                        {
                            layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000});
                            //setTimeout(function(){
                            //        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&customerid="+customerId;
                            //    }
                            //    ,1000);
                            setTimeout(function(){
                                    parent.layer.close(layerIndex);
                                }
                                ,1000);
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