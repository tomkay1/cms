/**
 * Created by Administrator on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addLinkForm").validate({
        rules: {
            title:{
                required: true,
            },
            description:{
                required: true,
            },
            linkUrl:{
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
                required:"请输入标题"
            },
            description:{
                required:"请输入描述"
            },
            linkUrl:{
                required:"请输入链接地址"
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
            if(f==""){
                layer.msg("请上传图片",{time: 2000});commonUtil.cancelDisabled("jq-cms-Save");
            }
            else if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f)) {
                layer.msg("请上传正确图片",{time: 2000});commonUtil.cancelDisabled("jq-cms-Save");
            }
            else{
            $.ajax({
                url: "/link/saveLink",
                data: {
                    id:$("#hidLinkID").val(),
                    title:$("#title").val(),
                    customerId:customerId,
                    linkUrl: $("#linkUrl").val(),
                    thumbUri: $("#thumbUri").val(),
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
                            //$("#title").val("");
                            //$("#linkUrl").val("");
                            //$("#thumbUri").val("");
                            //$("#uploadThumbUri").attr("src","");
                            //$("#description").val("");
                            //$("#categoryId").val("-1");
                            //$("#orderWeight").val("50");
                            layer.msg("操作成功",{time: 2000});
                            layer.msg("修改成功,2秒后将自动返回列表页面",{time: 2000})
                            commonUtil.cancelDisabled("jq-cms-Save");
                            window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&customerid="+customerId;
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

    $("#btnLinkFile").bind("change",function(){
        var btnFile=document.getElementById('btnLinkFile').getAttribute("id");
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


