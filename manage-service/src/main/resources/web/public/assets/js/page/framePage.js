define(function(require, exports, module) {
    require('../../css/bootstrap-table/bootstrap-table.min.css');

    exports.init = function () {
        require.async('../../libs/bootstrap-table.min.js', function () {
            require.async('../../libs/bootstrap-table-zh-CN.min.js', function () {
                var $table = $('#frameTable');
                $table.bootstrapTable({
                    url: '../data/data.json',
                    dataType: 'json',
                    locales: 'zh-CN',
                    striped: true,
                    pagination: true,
                    columns: [
                        {
                            width: '10%',
                            title: '序号',
                            align: 'center',
                            field: 'id'
                        }, {
                            width: '20%',
                            title: '页面名字',
                            align: 'center',
                            field: 'name'
                        }, {
                            width: '20%',
                            title: '创建时间',
                            align: 'center',
                            field: 'date'
                        }, {
                            width: '10%',
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            formatter:function(value,row,index){
                                return '<button class="btn btn-default btn-xs delete-btn" data-id="'+ row.id +'">删除</button>';
                            }
                        }, {
                            width: '10%',
                            title: '模板制作',
                            align: 'center',
                            field: 'id',
                            formatter:function(value,row,index){
                                return '<button class="btn btn-default btn-xs edit-btn" data-id="'+ row.id +'">编辑</button>';
                            }

                        }, {
                            width: '30%',
                            title: '页面地址',
                            field: 'url'
                        }
                    ]
                });
            });
        });
    };
});
