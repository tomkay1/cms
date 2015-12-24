/**
 * Created by Administrator on 2015/12/23.
 */
define(function (require, exports, module) {
    alert('f');
    $("#jq-cms-Save").validate({
        rules: {
            txtModelName:{
                required: true,
            },
            txtModelDescription:{
                maxlength:200
            },
            txtModelType: {
                selrequired: "-1"
            }
        },
        messages: {
            txtModelName:{
              required:"模型名称为必输项"
            },
            txtModelDescription:{
                maxlength:"模型描述不能超过200个字符"
            },
            txtModelType: {
                selrequired: "请选择模型类型"
            }
        },
        submitHandler: function (form, ev) {
            //editor.sync();
            //var commonUtil = require("Common");
            //commonUtil.setDisabled("save-btn");
            //$.ajax({
            //    url: "/ublog/addblog",
            //    data: {
            //        name: $("#ublog_title").val(),
            //        source: $("#ublog_resource").val(),
            //        typeid: $("#ublog_type").val(),
            //        tags: $("#ublog_tags").val(),
            //        description: $("#ublog_description").val(),
            //        content: $("#content").val()
            //    },
            //    type: "POST",
            //    dataType: 'json',
            //    success: function (data) {
            //        if (data != null) {
            //            switch (data.code) {
            //                case 200:
            //                    $("#alert-success").html("新增博客成功");
            //                    $("#alert-success").show();
            //                    break;
            //                case 500://保存失败
            //                    $("#alert-success").html("新增博客失败");
            //                    $("#alert-success").show();
            //                    break;
            //                case 402://没有登录
            //                    window.location.href = data.data.toString();
            //                    break;
            //                case 503://服务器错误
            //                    $("#alert-success").html("服务器繁忙,请稍后再试");
            //                    $("#alert-success").show();
            //                    break;
            //            }
            //        }
            //        else {
            //            $("#alert-success").html("服务器繁忙,请稍后再试");
            //            $("#alert-success").show();
            //        }
            //        commonUtil.cancelDisabled("save-btn");
            //    },
            //    error: function () {
            //        $("#alert-success").html("服务器繁忙,请稍后再试");
            //        $("#alert-success").show();
            //        commonUtil.cancelDisabled("save-btn");
            //    }
            //});
            return false;
        },
        invalidHandler: function () {
            return true;
        }
    });
})