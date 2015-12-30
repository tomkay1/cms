/**
 * Created by chendeyu on 2015/12/29.
 */
define(function (require, exports, module) {

    //TODO:初始化加载模型列表
    var RegionGrid= $("#js-RegionList").Grid({
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
        url: '/region/getRegionList',//数据来源Url|通过region自定义属性配置
        rows: [
            {
                width: '20%', field: 'regionCode', title: '地区编号', align: 'center'
            },
            { width: '20%', field: 'regionName', title: '地区名称', align: 'center' },
            { width: '20%', field: 'langCode', title: '语言编号', align: 'center' },
            { width: '15%', field: 'langName', title: '语言名称', align: 'center' },
            { width: '15%', field: 'langTag', title: '地区代码', align: 'center' },
            { width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='javascript:' class='js-hot-regionDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='/region/updateRegion?id="+rowData.id+"' target='content' style='margin-right:10px; color: blue'>修改</a>"
                }
            },

        ]
    },function(){
        deleteRegion();
    });

    //TODO:搜索
    $("#jq-cms-search").click(function(){
        var option={
            dataParam:{
                name:$("#regionName").val()
            }
        };
        RegionGrid.Refresh(option);
    })

    //TODO:显示所有
    $("#jq-cms-searchAll").click(function(){
        var option={
            dataParam:{
                name:""
            }
        };
        RegionGrid.Refresh(option);
    })

    //TODO:删除
    function deleteRegion(){
        var obj=$(".js-hot-regionDelete");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var layer=require("layer");
                layer.confirm('您确定要删除该地区吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/region/deleteRegion",
                        data: {
                            id:id
                        },
                        type: "POST",
                        dataType: 'json',
                        success: function (data) {
                            if(data!=null)
                            {
                                var code=parseInt(data.code);
                                switch(code)
                                {
                                    case 200:
                                        layer.msg("删除成功",{time: 2000});
                                        RegionGrid.reLoad();
                                        break;
                                    case 202:
                                        layer.msg("对不起,您没有删除权限",{time: 2000});
                                        break;
                                    default :
                                        layer.msg("系统繁忙,请稍后再试...",{time: 2000});
                                        break;
                                }
                            }
                        }
                    });
                });
            })
        })
    }
});