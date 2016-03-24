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
    var customerId =commonUtil.getQuery("customerId");
    var pageGrid=$("#tab1").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            siteId:$("#siteType").val(),
            delete:true
        },
        height:'auto',
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
                    if(value!=null)
                    {
                        return value.year+"-"+value.monthValue+"-"+value.dayOfMonth+" "+value.hour+":"+value.minute;
                    }
                    return "";
                }
            },
            {width: '30%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='#' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|"+
                        "<a href='javascript:void(0)'  style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|"+
                        "<a href='javascript:void(0)' style='color:#999 !important;margin-right:5px;margin-left:5px;' title='店铺主页'>店铺主页</a>|"+
                       " <a href='javascript:void(0)' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                }
            }
        ]
    });

    var pageGrid2=$("#tab2").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            siteId:$("#siteType").val(),
            delete:false
        },
        height:'auto',
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
                    if(value!=null)
                    {
                        return value.year+"-"+value.monthValue+"-"+value.dayOfMonth+" "+value.hour+":"+value.minute;
                    }
                    return "";
                }
            },
            {width: '30%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='#' style='color:#07d;margin-right:5px;margin-left:5px;' title='编辑'>编辑</a>|"+
                        "<a href='javascript:void(0)'  style='color:#07d;margin-right:5px;margin-left:5px;' title='丢草稿箱'>丢草稿箱</a>|"+
                        "<a href='javascript:void(0)' style='color:#999 !important;margin-right:5px;margin-left:5px;' title='店铺主页'>店铺主页</a>|"+
                        " <a href='javascript:void(0)' style='color:#07d !important;margin-right:5px;margin-left:5px;' title='链接'>链接</a>";
                }
            }
        ]
    });
});