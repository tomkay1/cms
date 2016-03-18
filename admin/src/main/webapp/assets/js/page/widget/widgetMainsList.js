/**
 * Created by chendeyu on 2016/3/17.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var customerId =commonUtil.getQuery("customerId");
    var layer=require("layer");
    var WidgetMainsGrid=$("#js-WidgetMainsList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            customerId:customerId
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/widget/getWidgetMainsList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'name', title: '主体名称', align: 'center'},
            {width: '30%', field: 'description', title: '主体描述', align: 'center'},
            {
                width: '20%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if(value!=null)
                    {
                        return value.year+"-"+value.monthValue+"-"+value.dayOfMonth+" "+value.hour+":"+value.minute;
                    }
                    return "";
                }
            },
            {width: '20%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return  "<a href='javascript:' class='js-hot-widgetMainsUpload' id='upload' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>上传</a>" +
                    "<a href='javascript:' class='js-hot-widgetMainsDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-widgetMainsUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    },function(){
        updateWidgetMains();
        deleteWidgetMains();
        uploadWidgetMains();
    });
//搜索
    $("#jq-cms-search").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        var option={
            dataParam:{
                name:$("#name").val(),
            }
        };
        WidgetMainsGrid.Refresh(option);
    })

    //显示所有
    $("#jq-cms-searchAll").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        var option={
            dataParam:{
                name:"",
            }
        };
        WidgetMainsGrid.Refresh(option);
    })

    //上传
    function uploadWidgetMains(){
        var obj=$(".js-hot-widgetMainsUpload");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                $("#upload").jacksonUpload({
                    url: "/cms/widgetUpLoad",
                    name: "btnFile1",
                    enctype: "multipart/form-data",
                    submit: true,
                    method: "post",
                    data:{
                        widgetId: id
                    },
                    callback: function (json) {
                        if(json!=null)
                        {
                            var code=parseInt(json.code);
                            switch (code){
                                case 200:
                                    layer.msg("操作成功", {
                                        icon: 1,
                                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                    }, function () {
                                        location.reload()
                                    });
                                    break;
                                case 403:
                                    layer.msg("文件格式错误", {
                                        icon: 2,
                                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                    })
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
            })
        })
    }


    //修改
    function updateWidgetMains(){
        var obj=$(".js-hot-widgetMainsUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerid");
                window.location.href="http://"+window.location.host+"/"+"widget/updateWidgetMains?id="+id+"&customerId="+customerId;
            })
        })
    }


    //删除
    function deleteWidgetMains(){
        var obj=$(".js-hot-widgetMainsDelete");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerid");
                layer.confirm('您确定要删除该主体吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/widget/deleteWidgetMains",
                        data: {
                            id:id,
                            customerId:customerId
                        },
                        type: "POST",
                        dataType: 'json',
                        success: function (data) {
                            if(data!=null)
                            {
                                var code=parseInt(data.code);
                                switch(code)
                                {
                                    case 200:
                                        layer.msg("删除成功", {
                                            icon: 1,
                                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                        }, function(){
                                            WidgetMainsGrid.reLoad();
                                        });
                                        break;
                                    case 202:
                                        layer.msg("对不起,您没有删除权限", {
                                            icon: 2,
                                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                        }, function(){
                                        });
                                        break;
                                    default :
                                        layer.msg("系统繁忙,请稍后再试...", {
                                            icon: 2,
                                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                                        }, function(){
                                        });
                                        break;
                                }
                            }
                        }
                    });
                });
            })
        })
    }
});