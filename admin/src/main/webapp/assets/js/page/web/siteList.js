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
        dataParam: {
            enabled: true
        },
        url: '/Smart/getSmartPage',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '30%', field: 'title', title: '站点名称', align: 'center'},
            {width: '30%', field: 'title', title: '站点标题', align: 'center'},
            {width: '30%', field: 'title', title: '站点域名', align: 'center'},
            {
                width: '20%', field: 'createdTime', title: '创建时间', align: 'left',
                formatter: function (value, rowData) {
                    return value.toString().toString().substr(0, 16).replace('T', ' ');
                }
            },
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                        return "<a href='#' style='margin-right:10px;'>删除</a>" +
                            "<a href='#' style='margin-right:10px;'>修改</a>"
                }
            },
        ]
    });
});