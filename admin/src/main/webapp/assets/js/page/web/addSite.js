/**
 * Created by chendeyu on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addSite").validate({
        rules: {
            name:{
                required: true,
            },
            title:{
                required: true,
            },
            description:{
                required: true,
            },
            keywords:{
                required: true,
            },
            logoUri:{
                required: true,
            },
            copyright:{
                required: true,
            },
            logoUri:{
                required: true,
            },
            hosts:{
                required: true,
            },
            txtModelDescription:{
                maxlength:200
            },
            region: {
                selrequired: "-1"
            },
        },
        messages: {
            name:{
                required:"站点名称为必输项"
            },
            title:{
                required:"站点标题为必输项"
            },
            description:{
                required:"站点描述为必输项"
            },
            copyright:{
                required:"版权信息为必输项"
            },
            keywords:{
                required:"关键字为必输项"
            },
            hosts:{
                required:"域名为必输项"
            },
            region: {
                selrequired: "请选择地区"
            },
        },
        submitHandler: function (form, ev) {
            //var commonUtil = require("common");
            //commonUtil.setDisabled("jq-cms-Save");
            //$.ajax({
            //    url: "/model/updateModel",
            //    data: {
            //        id:$("#hidModelID").val(),
            //        name: $("#txtModelName").val(),
            //        description: $("#txtModelDescription").val(),
            //        type: $("#txtModelType").val(),
            //        orderWeight: $("#txtOrderWeight").val()
            //    },
            //    type: "POST",
            //    dataType: 'json',
            //    success: function (data) {
            //        var layer=require("layer");
            //        if(data!=null)
            //        {
            //            var index=parseInt(data.code);
            //            if(index==200)
            //            {
            //                var layer=require("layer");
            //                layer.msg("操作成功",{time: 2000});
            //                $("#txtModelName").val("");
            //                $("#txtModelDescription").val("");
            //            }
            //            if(index==500)
            //                layer.msg("操作失败",{time: 2000})
            //        }
            //        commonUtil.cancelDisabled("jq-cms-Save");
            //    },
            //    error: function () {
            //        commonUtil.cancelDisabled("jq-cms-Save");
            //    }
            //});
            //return false;
            alert("1");
        },
        invalidHandler: function () {
            return true;
        }
    });
});