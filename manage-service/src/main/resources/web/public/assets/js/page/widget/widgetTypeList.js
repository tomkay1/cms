/**
 * Created by chendeyu on 2016/3/17.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var ownerId =commonUtil.getQuery("ownerId");
    var WidgetTypeGrid=$("#js-WidgetTypeList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            ownerId:ownerId
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/manage/widget/getWidgetTypeList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'name', title: '类型名称', align: 'center'},
            {width: '30%', field: 'scenes', title: '范畴类型', align: 'center',
                formatter:function(value,rowData){
                    if(value=="COMMON"){
                        return "公共";
                    }else if(value=="PC_WEBSITE"){
                        return "PC官网";
                    }else if(value=="PC_SHOP"){
                        return "PC商城"
                    }
                }
            },
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
                    return "<a href='javascript:' class='js-hot-widgetTypeDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-widgetUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    },function(){
        updateWidgetType();
        deleteWidgetType();
    });
//搜索
    $("#jq-cms-search").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        var option={
            dataParam:{
                name:$("#name").val(),
                ownerId:ownerId
            }
        };
        WidgetTypeGrid.Refresh(option);
    })

    //显示所有
    $("#jq-cms-searchAll").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        var option={
            dataParam:{
                name:"",
                ownerId:ownerId
            }
        };
        WidgetTypeGrid.Refresh(option);
    })

    var layer=require("layer");
    //新增
    $("#js-cms-addWidgetType").click(function(){
        layer.open({
            type: 2,
            title: "新增控件类型",
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '400px'],
            content: "/manage/widget/addWidgetType",
            end:function(){
                var option={
                    dataParam:{
                        ownerId:ownerId
                    }
                };
                WidgetTypeGrid.Refresh(option);
            }
        });
    })

    //修改
    function updateWidgetType(){
        var obj=$(".js-hot-widgetUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                layer.open({
                    type: 2,
                    title: "修改控件类型",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['600px', '400px'],
                    content: "/manage/widget/updateWidgetType?id="+id,
                    end:function(){
                        var option={
                            dataParam:{
                                ownerId:ownerId
                            }
                        };
                        WidgetTypeGrid.Refresh(option);
                    }
                });
            })
        })
    }



    //删除
    function deleteWidgetType(){
        var obj=$(".js-hot-widgetTypeDelete");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                var layer=require("layer");
                layer.confirm('您确定要删除该类型吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/manage/widget/deleteWidgetType",
                        data: {
                            id:id,
                            ownerId:ownerId
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
                                            WidgetTypeGrid.reLoad();
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