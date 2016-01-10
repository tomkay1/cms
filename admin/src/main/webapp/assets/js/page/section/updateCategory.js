/**
 * Created by xhl on 2016/1/10.
 */
define(function (require, exports, module) {
    $("#updateColumnForm").validate({
        rules: {
            categoryName:{
                required: true
            },
            route:{
                remote: {
                    url: "/route/isExistsRouteBySiteAndRuleIgnore",     //后台处理程序
                    type: "post",               //数据发送方式
                    dataType: "json",           //接受数据格式
                    data: {                     //要传递的数据
                        siteId:$("#siteId").val(),
                        noRule:$("#route").val(),
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
            template:{
                route:true
            }
        },
        messages: {
            categoryName:{
                required:"请输入栏目名称"
            },
            route:{
                route:"请使用字母、数字、下划线、反斜杠，且长度为1至20个字符",
                remote: "该栏目路由已经存在"
            },
            template:{
                route:"请使用字母、数字、下划线、反斜杠，且长度为1至20个字符",
            }
        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $.ajax({
                url: "/category/modifyCategory",
                data: {
                    id:$("#hidCategoryID").val(),
                    siteId:$("#siteId").val(),
                    name: $("#categoryName").val(),
                    modelId: $("#modelId").val(),
                    orderWeight: $("#orderWeight").val(),
                    rule:$("#route").val(),
                    template:$("#template").val(),
                    noRule:$("#noRule").val()
                },
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    var layer=require("layer");
                    var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    if(data!=null)
                    {
                        var index=parseInt(data.code);
                        switch (index){
                            case 200:
                                $("#route").val("");
                                $("#template").val("")
                                parent.layer.close(layerIndex);
                                layer.msg("修改成功",{time: 2000});
                                break;
                            case 204:
                                layer.msg("路由规则已经存在",{time: 2000});
                                break;
                            case 500:
                                layer.msg("操作失败",{time: 2000})
                                break;
                        }
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