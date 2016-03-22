/**
 * Created by chendeyu on 2015/12/24.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var customerId =commonUtil.getQuery("customerId");
    var layer=require("layer");
    var GalleryListGrid=$("#js-GalleryList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            customerId:customerId,
            galleryId:$("#galleryId").val()
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/gallery/getGalleryList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '10%', field: 'id', title: '图片id', align: 'center'},
            {width: '10%', field: 'size', title: '图片规格', align: 'center'},
            {width: '60%', field: 'thumbUri', title: '缩略图', align: 'center',
                formatter: function (value, rowData) {
                    return "<img src='"+value+"'/>" ;
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
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return"<a href='javascript:' class='js-hot-galleryListDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-galleryListUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    },function(){
        updateGalleryList();
        deleteGalleryList();
    });





    //修改
    function updateGalleryList(){
        var obj=$(".js-hot-galleryListUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                window.location.href="http://"+window.location.host+"/"+"gallery/updateGalleryList?id="+id+"&customerId="+customerId;
            })
        })
    }


    //删除
    function deleteGalleryList(){
        var obj=$(".js-hot-galleryListDelete");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                layer.confirm('您确定要删除该图片吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/gallery/deleteGalleryList",
                        data: {
                            id:id,
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
                                            GalleryListGrid.reLoad();
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