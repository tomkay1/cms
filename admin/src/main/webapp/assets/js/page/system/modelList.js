/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $("#js-ModelList").Grid({
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
        url: '/model/getModelList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {
                width: '30%', field: 'name', title: '模型名称', align: 'center'
            },
            { width: '40%', field: 'description', title: '描述', align: 'center' },
            {
                width: '20%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if(value!=null)
                    {
                        return value.toString();
                    }
                    return "";
                }
            },
            { width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='#' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='#' style='margin-right:10px; color: blue'>修改</a>"
                }
            },

        ]
    });
});