/**
 * Created by chendeyu on 2016/1/11.
 */
define(function (require, exports, module) {
    $("#updateVideoForm").validate({
        rules: {
            title:{
                required: true,
            },
            description:{
                required: true,
            },
            outLinkUrl:{
                required: true,
                url:true
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
                required:"请输入描述"
            },
            outLinkUrl:{
                required: "请输入视频链接网址",
                url:"请输入正确的视频链接网址"
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
            var f=$("#thumbUri").val();
            if(f!=""&&!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f)) {
                layer.msg("请上传正确图片",{time: 2000});commonUtil.cancelDisabled("jq-cms-Save");
            }
            else{
                $.ajax({
                    url: "/video/saveVideo",
                    data: {
                        id:$("#hidVideoID").val(),
                        title:$("#title").val(),
                        customerId:customerId,
                        thumbUri: $("#thumbUri").val(),
                        description: $("#description").val(),
                        outLinkUrl: $("#outLinkUrl").val(),
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
                                layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000})
                                setTimeout(function(){
                                        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&customerid="+customerId;
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
                })};
            return false;
        },
        invalidHandler: function () {
            return true;
        }
    });

    $("#btnVideoFile").bind("change",function(){
        var btnFile=document.getElementById('btnVideoFile').getAttribute("id");
        uploadImg(btnFile);
    })

    function uploadImg (btnFile) {
        layer.msg("正在上传", {time: 2000});
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        $.ajaxFileUpload({
            url: "/cms/impUpLoad",
            secureuri: false,//安全协议
            fileElementId: btnFile,//id
            dataType: 'json',
            type: "post",
            data:{
                customerId: customerId
            },
            error: function (data, status, e) {

            },
            success: function (json) {
                if (json.result == 1) {
                    $("#uploadThumbUri").attr("src", json.fileUrl);
                    $("#thumbUri").val(json.fileUri);
                    commonUtil.cancelDisabled("jq-cms-Save");
                    layer.msg("操作成功", {time: 2000});
                } else {
                    layer.msg("操作失败", {time: 2000});
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
            }
        });
    }


});



