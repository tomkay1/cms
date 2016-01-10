/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var customerId =commonUtil.getQuery("customerId");
    var SiteGrid=$("#js-RouteList").Grid({
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
        url: '/route/getRouteList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'rule', title: '路由规则', align: 'center'},
            {width: '30%', field: 'template', title: '路由模版', align: 'center'},
            {width: '30%', field: 'description', title: '描述', align: 'center'},
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
                    return "<a href='javascript:' class='js-hot-siteDelete' data-id='"+rowData.siteId+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-siteUpdate' data-id='"+rowData.siteId+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    },function(){
        updateSite();
        deleteSite();
    });
//TODO:搜索
    $("#jq-cms-search").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        var option={
            dataParam:{
                name:$("#siteName").val(),
                customerId:customerId
            }
        };
        SiteGrid.Refresh(option);
    })

    //TODO:显示所有
    $("#jq-cms-searchAll").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        var option={
            dataParam:{
                name:"",
                customerId:customerId
            }
        };
        SiteGrid.Refresh(option);
    })

    //TODO:修改
    function updateSite(){
        var obj=$(".js-hot-siteUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerid");
                window.location.href="http://"+window.location.host+"/"+"site/updateSite?id="+id+"&customerId="+customerId;
            })
        })
    }

    //TODO:删除
    function deleteSite(){
        var obj=$(".js-hot-siteDelete");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerid");
                var layer=require("layer");
                layer.confirm('您确定要删除该站点吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/site/deleteSite",
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
                                        layer.msg("删除成功",{time: 2000});
                                        SiteGrid.reLoad();
                                        break;
                                    case 202:
                                        layer.msg("对不起,您没有删除权限",{time: 2000});
                                        break;
                                    default :
                                        layer.msg("系统繁忙,请稍后再试...",{time: 2000});
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