/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {
    $("#addColumnForm").validate({
        rules: {
            categoryName:{
                required: true
            },
            route:{
                remote: {
                    url: "/route/isExistsRouteBySiteAndRule",     //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {                     //要传递的数据
                        siteId:$("#siteId").val(),
                        rule: function () {
                            return $("#route").val();
                        }
                    }
                },
                route:true
            },
            modelType: {
                selrequired: "-1"
            },
        },
        messages: {
            categoryName:{
                required:"请输入栏目名称"
            },
            route:{
                route:"请使用字母、数字、下划线、反斜杠，且长度为1至20个字符",
                remote: "该栏目路由已经存在"
            },
            modelType:{
                selrequired: "请选择模型类型"
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/category/saveCategory",
                data: {
                    id:$("#hidRegionID").val(),
                    siteId:$("#siteId").val(),
                    parentId: $("#parentId").val(),
                    name: $("#name").val(),
                    model: $("#modelType").val(),
                    orderWeight: $("#orderWeight").val(),
                    route:$("#route").val()
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
                            $("#name").val("");
                            $("#modelType").val("");
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