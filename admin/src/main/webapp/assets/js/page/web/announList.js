/**
 * Created by chendeyu on 2015/12/24.
 */
define(function (require, exports, module) {
    $("#js-announList").Grid({
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
            {width: '20%', field: 'title', title: '所属栏目节点', align: 'center'},
            { width: '40%', field: 'title', title: '公告内容', align: 'center' },
            {
                width: '30%', field: 'createdTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    return value.toString().toString().substr(0, 16).replace('T', ' ');
                }
            },
            { width: '10%', field: 'title', title: '操作', align: 'center' }
        ]
    });
});