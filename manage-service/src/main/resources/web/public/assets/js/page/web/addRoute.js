/**
 * Created by xhl on 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addRouteForm").validate({
        rules: {
            routeName:{
                required: true,
            },
            routeRule:{
                required: true,
                route:true,
                remote: {
                    url: "/route/isExistsRouteBySiteAndRule",     //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {                     //要传递的数据
                        siteId:$("#siteId").val(),
                        rule: function () {
                            return $("#routeRule").val();
                        }
                    }
                },
            },
            template:{
                required: true,
                route:true
            }
        },
        messages: {
            routeName:{
                required:"请输入路由描述信息"
            },
            routeRule:{
                required:"请输入页面路由规则",
                route:"请使用非中文字符，且长度为1至50个字符",
                remote: "该栏目路由已经存在,或者内置关键字"
            },
            template:{
                required:"请输入路由模版",
                route:"请使用非中文字符，且长度为1至50个字符",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/route/saveRoute",
                data: {
                    siteId:$("#siteId").val(),
                    routeName: $("#routeName").val(),
                    routeRule: $("#routeRule").val(),
                    template:$("#template").val(),
                    routeType:$("#routeType").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer=require("layer");
                    if(data!=null)
                    {
                        var index=parseInt(data.code);
                        switch (index){
                            case 200:
                                $("#routeName").val("");
                                $("#routeRule").val("");
                                $("#routeRule").val("");
                                $("#template").val("")
                                layer.msg("添加成功",{time: 2000});
                                break;
                            case 204:
                                layer.msg("路由规则已经存在",{time: 2000});
                                break;
                            case 405:
                                layer.msg("信息不存在",{time: 2000})
                                break;
                            case 500:
                                layer.msg("操作失败",{time: 2000})
                                break;
                            case 502:
                                layer.msg("系统繁忙,请稍后再试",{time: 2000})
                                break;
                        }
                    }
                    commonUtil.cancelDisabled("jq-cms-Save");
                },
                error: function () {
                    layer.msg("系统繁忙,请稍后再试",{time: 2000})
                    commonUtil.cancelDisabled("jq-cms-Save");
                }
            });
            return false;
        },
        invalidHandler: function () {
            return true;
        },
    });
});
