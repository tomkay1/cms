/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#js-SiteList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/site/getSiteList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'name', title: '站点名称', align: 'center'},
            {width: '30%', field: 'title', title: '站点标题', align: 'center'},
            {width: '30%', field: 'description', title: '站点描述', align: 'center'},
            {
                width: '20%', field: 'createTime', title: '更新时间', align: 'left',
                formatter: function (value, rowData) {
                    //
                    if(value!=null)
                    {
                        return value.toString();
                    }
                    return "";
                }
            },
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='#' style='margin-right:10px;'>删除</a>" +
                        "<a href='#' style='margin-right:10px;'>修改</a>"
                }
            }
        ]
    });
});