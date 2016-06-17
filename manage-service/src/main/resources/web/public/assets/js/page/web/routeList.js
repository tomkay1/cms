/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var ownerId =commonUtil.getQuery("ownerId");
    commonUtil.getSiteList(ownerId,"jq-cms-siteList");
    var SiteGrid=$("#js-RouteList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            ownerId:ownerId,
            siteId:$("#jq-cms-siteList").val()
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/route/getRouteList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'rule', title: '路由规则', align: 'left'},
            {width: '30%', field: 'template', title: '路由模版', align: 'left'},
            {width: '30%', field: 'description', title: '描述', align: 'left'},
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
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='javascript:' class='js-hot-routeUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;' title='修改'>修改</a>" +
                        "<a href='javascript:' class='js-hot-routeDelete' data-id='"+rowData.id+"' style='margin-right:10px; color: blue' title='删除'>删除</a>"
                }
            }
        ]
    },function(){
        updateRoute();
        deleteRoute();
    });
    $("#jq-cms-siteList").on("change",function(){
        var option={
            dataParam:{
                ownerId:ownerId,
                siteId:$("#jq-cms-siteList").val()
            }
        };
        SiteGrid.Refresh(option);
    })
    $("#jq-cms-search").click(function(){
        var option={
            dataParam:{
                ownerId:ownerId,
                description:$("#description").val(),
                siteId:$("#jq-cms-siteList").val()
            }
        };
        SiteGrid.Refresh(option);
    })

    $("#jq-cms-searchAll").click(function(){
        var option={
            dataParam:{
                ownerId:ownerId,
                description:"",
                siteId:$("#jq-cms-siteList").val()
            }
        };
        SiteGrid.Refresh(option);
    })

    function Refresh(){
        var option={
            dataParam:{
                ownerId:ownerId,
                description:$("#description").val(),
                siteId:$("#jq-cms-siteList").val()
            }
        };
        SiteGrid.Refresh(option);
    }

    var layer=require("layer");
    //新增
    $("#js-cms-addRoute").click(function(){
        var siteId=$("#jq-cms-siteList").val();
        layer.open({
            type: 2,
            title: "新增路由规则",
            shadeClose: true,
            shade: 0.8,
            area: ['900px', '500px'],
            content: "/route/addRoute?siteId="+siteId,
            end:function(){
                Refresh();
            }
        });
    })

    //修改
    function updateRoute(){
        var obj=$(".js-hot-routeUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                layer.open({
                    type: 2,
                    title: "修改路由规则",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['900px', '500px'],
                    content: "/route/updateRoute?id="+id,
                    end:function(){
                       Refresh();
                    }
                });
            })
        })
    }

    //删除
    function deleteRoute(){
        var obj=$(".js-hot-routeDelete");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                layer.confirm('您确定要删除该路由？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    $.ajax({
                        url: "/route/deleteRoute",
                        data: {
                            id: id
                        },
                        type: "POST",
                        dataType: 'json',
                        success: function (data) {
                            if(data!=null) {
                                var code = parseInt(data.code);
                                switch (code) {
                                    case 200:
                                        layer.msg("删除成功",{time: 2000});
                                        Refresh();
                                        break;
                                    case 500:
                                        layer.msg("删除失败",{time: 2000});
                                        break;
                                    case 202:
                                        layer.msg("对不起,您没有删除权限",{time: 2000});
                                        break;
                                    case 205:
                                        layer.msg("该路由存在栏目关联信息,不能删除",{time: 2000});
                                        break;
                                    case 502:
                                        layer.msg("系统繁忙,请稍后再试...",{time: 2000});
                                        break;
                                }
                            }
                        },
                        error:function(){
                            layer.msg("网络出现异常,请稍后再试...",{time: 2000});
                        }
                    });
                });
            })
        })
    }
});