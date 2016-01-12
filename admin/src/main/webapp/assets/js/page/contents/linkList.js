/**
 * Created by chendeyu on 2015/12/24.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var customerId =commonUtil.getQuery("customerId");
    var LinkGrid=$("#js-LinkList").Grid({
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
        url: '/link/getLinkList',//数据来源Url|
        rows: [
            {width: '20%', field: 'categoryName', title: '所属栏目', align: 'center'},
            {width: '20%', field: 'title', title: '标题', align: 'center'},
            {width: '25%', field: 'description', title: '描述', align: 'center'},
            {
                width: '15%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if(value!=null)
                    {
                        return value.year+"-"+value.monthValue+"-"+value.dayOfMonth+" "+value.hour+":"+value.minute;
                    }
                    return "";
                }
            }
            ,
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='javascript:' class='js-hot-linkDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-linkUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    },function(){
        updateLink();
        deleteLink();
    });
//TODO:搜索
    $("#jq-cms-search").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var customerId =commonUtil.getQuery("customerId");
        var option={
            dataParam:{
                title:$("#linkName").val(),
                customerId:customerId
            }
        };
        LinkGrid.Refresh(option);
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
        LinkGrid.Refresh(option);
    })

    //TODO:修改
    function updateLink(){
        var obj=$(".js-hot-linkUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerId");
                window.location.href="http://"+window.location.host+"/"+"link/updateLink?id="+id+"&customerId="+customerId;
            })
        })
    }


    //TODO:删除
    function deleteLink(){
        var obj=$(".js-hot-linkDelete");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var customerId = commonUtil.getQuery("customerId");
                var layer=require("layer");
                layer.confirm('您确定要删除该站点吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/link/deleteLink",
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
                                        LinkGrid.reLoad();
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
