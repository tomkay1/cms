/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#usual1 ul").idTabs();
    $(".select1").uedSelect({
        width: 150
    });

    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var layer = require("layer");
    var customerId = commonUtil.getQuery("customerId");
    var pageGrid = $("#tab1").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam: {
            siteId: $("#siteType").val(),
            delete: false,
            publish: true
        },
        height: 'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/page/getPagesList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '40%', field: 'name', title: '页面名称', align: 'center'},
            {
                width: '30%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if (value != null) {
                        return value.year + "-" + value.monthValue + "-" + value.dayOfMonth + " " + value.hour + ":" + value.minute;
                    }
                    return "";
                }
            },
            {
                width: '30%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    if(rowData.home.toString()=='true'){
                        return "<a href='/customPages/" + rowData.id + "?customerid=" + rowData.customerId + "&siteId=" + $("#siteType").val() + "' target='content'' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|" +
                            "<a href='javascript:void(0)' class='js-pages-delete' data-id='"+rowData.id+"'  data-publish='false' style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|" +
                            "<a href='javascript:void(0)' style='color:#ccc !important;margin-right:5px;margin-left:5px;' title='店铺主页'>店铺主页</a>|" +
                            "<a href='javascript:void(0)' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                    }else{
                        return "<a href='/customPages/" + rowData.id + "?customerid=" + rowData.customerId + "&siteId=" + $("#siteType").val() + "' target='content'' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|" +
                            "<a href='javascript:void(0)' class='js-pages-delete' data-id='"+rowData.id+"'  data-publish='false' style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|" +
                            "<a href='javascript:void(0)' class='js-pages-home' data-id='"+rowData.id+"' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='设为主页'>设为主页</a>|" +
                            "<a href='javascript:void(0)' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                    }
                }
            }
        ]
    },function(){
        pageList.deleteBind();
        pageList.homeBind();
    });

    var pageGrid2 = $("#tab2").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam: {
            siteId: $("#siteType").val(),
            delete: false,
            publish: false
        },
        height: 'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/page/getPagesList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '40%', field: 'name', title: '页面名称', align: 'center'},
            {
                width: '30%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if (value != null) {
                        return value.year + "-" + value.monthValue + "-" + value.dayOfMonth + " " + value.hour + ":" + value.minute;
                    }
                    return "";
                }
            },
            {
                width: '30%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='/customPages/" + rowData.id + "?customerid=" + rowData.customerId + "&siteId=" + $("#siteType").val() + "' target='content'' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|" +
                        "<a href='javascript:void(0)' class='js-pages-delete' data-id='"+rowData.id+"' data-publish='true' style='color:#07d;margin-right:5px;margin-left:5px;' title='发布'>发布</a>|" +
                        "<a href='javascript:void(0)' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                }
            }
        ]
    },function(){
        pageList.deleteBind();
        pageList.homeBind();
    });

    var obj = $(".js-cms-defaults");
    $.each(obj, function (item, dom) {
        $(dom).click(function () {
            var customerId = $(dom).data("customerid");
            var urlFormatter = $(dom).data("url");
            var siteId = $("#siteType").val();
            var url = commonUtil.formatString(urlFormatter, customerId, siteId);
            window.location.href = url;
        })
    })

    var pageList = {
        deletePage: function (id,publish) {
            $.ajax({
                url: '/page/delete',
                type: "POST",
                dataType: 'json',
                data:{publish:publish,id:id},
                success: function (data) {
                    if(data!=null){
                        if(data.code==200){
                            layer.msg("页面丢弃草稿箱成功");
                            pageGrid.Refresh();
                            pageGrid2.Refresh();
                        }else{
                            layer.msg("页面丢弃草稿箱失败");
                        }
                    }else{
                        layer.msg("页面丢弃草稿箱失败");
                    }
                },error:function(){
                    layer.msg("页面丢弃草稿箱成功");
                }
            });
        },
        homePage:function(id){
            $.ajax({
                url: '/page/home',
                type: "POST",
                dataType: 'json',
                data:{id:id},
                success: function (data) {
                    if(data!=null){
                        if(data.code==200){
                            layer.msg("设置成功");
                            pageGrid.Refresh();
                            pageGrid2.Refresh();
                        }else{
                            layer.msg("设置失败,请稍后再试...");
                        }
                    }else{
                        layer.msg("服务器繁忙,请稍后再试...");
                    }
                },error:function(){
                    layer.msg("服务器繁忙,请稍后再试...");
                }
            });
        },
        deleteBind:function(){
            var obj=$(".js-pages-delete");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).data('id');
                    var publish=$(dom).data('publish');
                    pageList.deletePage(id,publish);
                });
            });
        },
        homeBind:function(){
            var obj=$(".js-pages-home");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).data("id");
                    pageList.homePage(id);
                });
            })
        }
    };
});