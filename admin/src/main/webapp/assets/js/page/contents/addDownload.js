/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addDownloadForm").validate({
        rules: {
            title:{
                required: true,
            },
            downloadUrl:{
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
                required:"请输入文件名称"
            },
            downloadUrl:{
                required:"请输入下载地址"
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
                url: "/download/saveDownload",
                data: {
                    id:$("#hidDownloadID").val(),
                    title:$("#title").val(),
                    customerId:customerId,
                    downloadUrl: $("#downloadUrl").val(),
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
                            layer.msg("保存成功！", {time: 2000})
                            $("#title").val("");
                            $("#downloadUrl").val("");
                            $("#description").val("");
                            $("#orderWeight").val("50")
                            //layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000})
                            //setTimeout(function(){
                            //        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&customerid="+customerId;
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



