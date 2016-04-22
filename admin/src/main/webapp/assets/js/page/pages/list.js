/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#usual1 ul").idTabs();
    $(".select1").uedSelect({
        width: 200
    });
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var layer = require("layer");
    var isDebug=commonUtil.isDebug();
    var rootUrl=commonUtil.getWebRoot($("#siteType").val(),isDebug);
    var scope=commonUtil.getQuery("scope");
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
                            "<a href='javascript:void(0)' class='js-pages-publish' data-id='"+rowData.id+"'  data-publish='false' style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|" +
                            "<a href='javascript:void(0)' style='color:#ccc !important;margin-right:5px;margin-left:5px;' title='店铺主页'>店铺主页</a>|" +
                            "<a href='javascript:void(0)' class='js-link-open' id='"+rowData.id+"'  style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                    }else{
                        return "<a href='/customPages/" + rowData.id + "?customerid=" + rowData.customerId + "&siteId=" + $("#siteType").val() + "' target='content'' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|" +
                            "<a href='javascript:void(0)' class='js-pages-publish' data-id='"+rowData.id+"'  data-publish='false' style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|" +
                            "<a href='javascript:void(0)' class='js-pages-home' data-id='"+rowData.id+"' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='设为主页'>设为主页</a>|" +
                            "<a href='javascript:void(0)' class='js-link-open' id='"+rowData.id+"' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                    }
                }
            }
        ]
    },function(){
        pageList.publishBind();
        pageList.homeBind();
        pageList.linkBind();
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
                        "<a href='javascript:void(0)' class='js-pages-publish'  data-id='"+rowData.id+"' data-publish='true' style='color:#07d;margin-right:5px;margin-left:5px;' title='发布'>发布</a>|" +
                        "<a href='javascript:void(0)' class='js-pages-delete' data-id='"+rowData.id+"'  style='color:#07d !important;margin-right:5px;margin-left:5px;' title='删除'>删除</a>"+
                        "<a href='javascript:void(0)' class='js-link-open' id='"+rowData.id+"' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                }
            }
        ]
    },function(){
        pageList.publishBind();
        pageList.homeBind();
        pageList.deleteBind();
        pageList.linkBind();
    });

    var obj = $(".js-cms-defaults");
    $.each(obj, function (item, dom) {
        $(dom).click(function () {
            var customerId = $(dom).data("customerid");
            var urlFormatter = $(dom).data("url")+"&scope="+scope;
            var siteId = $("#siteType").val();
            var url = commonUtil.formatString(urlFormatter, customerId, siteId);
            window.location.href = url;
        })
    })

    var pageList = {
        publishPage: function (id,publish) {
            var msgTitle=publish?"页面发布":"页面丢弃草稿箱";
            $.ajax({
                url: '/page/publish',
                type: "POST",
                dataType: 'json',
                data:{publish:publish,id:id},
                success: function (data) {
                    if(data!=null){
                        if(data.code==200){
                            layer.msg(msgTitle+"成功");
                            pageGrid.Refresh();
                            pageGrid2.Refresh();
                        }else{
                            layer.msg(msgTitle+"失败");
                        }
                    }else{
                        layer.msg(msgTitle+"失败");
                    }
                },error:function(){
                    layer.msg("服务器繁忙,请稍后再试...");
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
        publishBind:function(){
            var obj=$(".js-pages-publish");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).data('id');
                    var publish=$(dom).data('publish');
                    pageList.publishPage(id,publish);
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
        },
        search:function(){
            var option = {
                siteId: $("#siteType").val(),
                delete: false,
                publish: true,
                name:$("#pageName").val()
            }
            var option2={
                siteId: $("#siteType").val(),
                delete: false,
                publish: false,
                name:$("#pageName").val()
            }
            pageGrid.reLoad(option);
            pageGrid2.reLoad(option);
        },
        deletePage:function(id){
            $.ajax({
                url: '/page/delete',
                type: "POST",
                dataType: 'json',
                data:{id:id},
                success: function (data) {
                    if(data!=null){
                        if(data.code==200){
                            layer.msg("删除页面成功");
                            pageGrid.Refresh();
                            pageGrid2.Refresh();
                        }else{
                            layer.msg("删除页面失败");
                        }
                    }else{
                        layer.msg("删除页面失败");
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
                    var id=$(dom).data("id");
                    pageList.deletePage(id);
                });
            })
        },
        linkBind:function(){
            var obj=$(".js-link-open");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).attr('id');
                    pageList.linkOpen(id);
                });
            });
        },
        linkOpen:function(id){
            pageList.linkCal();
            var _defaultID = (new Date()).getTime();
            //var smartUrl = J.FormatString(apiConfig.msiteRoot+apiConfig.smartUrl, apiConfig.customid, url);
            var smartUrl=rootUrl+"shop/"+id;
            var _html = "<div class=\"dropdowns\" id=\"" + _defaultID + "\"><div class=\"dropdown-toggle\"  data-toggle=\"dropdowns\" aria-haspopup=\"true\" aria-expanded=\"false\"></div>" +
                "<div class=\"dropdown-menu\" role=\"menu\" style=\"background:#F5F5F5; border:none;top:auto;left:auto; box-shadow:none;\">" +
                "<div class=\"popover popover-link-wrap\" style=\"display: block;\">" +
                "<div class=\"arrow\"></div>" +
                "<div class=\"popover-inner popover-link\">" +
                "<div class=\"popover-content\">" +
                "<div class=\"form-inline\" title='点击输入框按ctrl+A全选再复制'>" +
                "<input type=\"text\" class=\"link-placeholder js-link-placeholder\"  style=\"height:28px; width:240;\" value='" + smartUrl + "'/>" +
                    //"<button type=\"button\" data-clipboard-text=\"" + smartUrl + "\" class=\"btn js-hot-clip active\" id=\"copy_"+_defaultID+"\">复制</button>" +
                "<button type=\"reset\" class=\"btnLink js-btn-cancel\">取消</button>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div></div>";
            $("#" + id).after(_html);
            $("#" + _defaultID).addClass("open");
            pageList.linkCallBind();
        },
        linkCal:function(id){
            if (typeof id =="undefined")
                $(".dropdowns").removeClass("open");
            else
                $("#" + id).removeClass("open");
        },
        linkCallBind:function(){
            var obj=$(".js-btn-cancel");
            obj.unbind("click");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    pageList.linkCal();
                });
            });
        }
    };
    $("#js-btn-search").click(function(){
        pageList.search();
    });
    $("#siteType").change(function(){
        pageList.search();
        initHomePage();
    })

    function initHomePage(){
        commonUtil.getHomePages(function(data){
            alert(data.code);
            window.console.log(data);
        },$("#siteType").val())
    }
    initHomePage();
});