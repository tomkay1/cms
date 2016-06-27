define(function(require, exports, module) {
    require('../../css/bootstrap-table/bootstrap-table.min.css');

    exports.init = function () {
        require.async('../../libs/bootstrap-table.min.js', function () {
            require.async('../../libs/bootstrap-table-zh-CN.min.js', function () {
                var $table = $('#modelTable');
                $table.bootstrapTable({
                    url: '../data/data1.json',
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
                            width: '30%',
                            title: '标题',
                            align: 'center',
                            field: 'name'
                        }, {
                            width: '10%',
                            title: '数据源名称',
                            align: 'center',
                            field: 'model'
                        }, {
                            width: '10%',
                            title: '数据模型',
                            align: 'center',
                            field: 'model'
                        }, {
                            width: '20%',
                            title: '创建时间',
                            align: 'center',
                            field: 'date'
                        }, {
                            width: '20%',
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            formatter: function(value,row,index){
                                var a = '<button class="btn btn-default btn-xs delete-btn" data-id="'+ row.id +'">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;';
                                var b = '<button class="btn btn-default btn-xs edit-btn" data-id="'+ row.id +'">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;';
                                var c = '<button class="btn btn-default btn-xs detail-btn" data-id="'+ row.id +'">查看详情</button>';
                                return a + b + c;
                            }
                        }
                    ]
                });
            });
        });
        $('#model-list').on('change', function () {
            var i = $(this).val();
            $('#modelTypeBtn').attr('data-target','#myModal' + i);
        });

        $('#tagBtn').on('click', function () {
            var flag = false;
            $('.productTag').each(function (i, v) {
                flag = $(v).val() ? true : false;
            });
            if (flag) $('<input type="text" class="form-control productTag">').insertBefore($('#tagBtn'));
        });
    };
});
