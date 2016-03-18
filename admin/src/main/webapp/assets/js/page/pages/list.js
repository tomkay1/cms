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
    var SiteGrid=$("#tab1").Grid({
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
        url: '/site/getSiteList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'name', title: '站点名称', align: 'center'},
            {width: '30%', field: 'title', title: '站点标题', align: 'center'},
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
                    return "<a href='javascript:' class='js-hot-siteDelete' data-id='"+rowData.siteId+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-siteUpdate' data-id='"+rowData.siteId+"' style='margin-right:10px; color: blue'>修改</a>"
                }
            }
        ]
    });
});