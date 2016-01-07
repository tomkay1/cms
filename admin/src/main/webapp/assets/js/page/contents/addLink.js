/**
 * Created by Administrator on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addLinkForm").validate({
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
            var customerId =commonUtil.getQuery("customerId");
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
                            $("#title").val("");
                            $("#linkUrl").val("");
                            $("#thumbUri").val("");
                            $("#uploadThumbUri").attr("src","");
                            $("#description").val("");
                            $("#categoryId").val("-1");
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

function uploadImg (btnFile, showImgId, pathId)
{
    layer.msg("正在上传", {time: 2000});
    $.ajaxFileUpload({
        url: "/cms/contentsUpload",
        secureuri: false,//安全协议
        fileElementId: btnFile,//id
        dataType: 'json',
        type: "post",
        data: null,
        error: function (data, status, e) {

        },
        success: function (json) {
            if (json.result == 1) {
                $("#" + showImgId).attr("src", json.fileUrl);
                $("#" + pathId).val(json.fileUri);
                layer.msg("操作成功", {time: 2000});
            } else {
                layer.msg("操作失败", {time: 2000});
            }
        }
    });
}

