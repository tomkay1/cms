/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var ownerId =commonUtil.getQuery("ownerId");
    $("#updateLinkForm").validate({
        rules: {
            title:{
                required: true,
            },
            linkUrl:{
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
            linkUrl:{
                required:"请输入链接地址",
                url:"请输入正确的链接地址"
            },
            categoryId: {
                selrequired: "请选择栏目"
            },
            OrderWeight:{
                digits:"请输入数字",
            }
        },
        submitHandler: function (form, ev) {
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/manage/link/saveLink",
                data: {
                    id:$("#hidLinkID").val(),
                    title:$("#title").val(),
                    ownerId:ownerId,
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
                    var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    if(data!=null)
                    {
                        var index=parseInt(data.code);

                        if(index==200)
                        {
                            layer.msg("操作成功,2秒后将自动返回列表页面",{time: 2000});
                            //setTimeout(function(){
                            //        window.location.href="http://"+window.location.host+"/"+"contents/contentsList?&ownerId="+ownerId;
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


    //上传图片模块
    var uploadModule={
        uploadImg:function(){
            $("#btnFile").jacksonUpload({
                url: "/manage/cms/imgUpLoad",
                name: "btnFile",
                enctype: "multipart/form-data",
                submit: true,
                method: "post",
                data:{
                    ownerId: ownerId
                },
                callback: function (json) {
                    if(json!=null)
                    {
                        var code=parseInt(json.code);
                        switch (code){
                            case 200:
                                $("#uploadThumbUri").attr("src", json.data.fileUrl);
                                $("#thumbUri").val(json.data.fileUri);
                                commonUtil.cancelDisabled("jq-cms-Save");
                                layer.msg("操作成功", {time: 2000});
                                break;
                            case 403:
                                layer.msg("文件格式错误,请上传jpg, jpeg,png,gif,bmp格式的图片", {time: 2000});
                                break;
                            case 502:
                                layer.msg("服务器错误,请稍后再试", {time: 2000});
                                break;
                        }
                    }
                },
                timeout: 30000,
                timeout_callback: function () {
                    layer.msg("图片上传操作", {time: 2000});
                }
            });
        }
    }

    uploadModule.uploadImg();


    $("#jq-cms-return").click(function(){
        var layer=require("layer");
        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(layerIndex);
    })
});