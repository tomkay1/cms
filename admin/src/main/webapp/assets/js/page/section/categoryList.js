/**
 * Created by xhl on 2015/12/30.
 */
define(function (require, exports, module) {
    var jqxcore=require("jqxcore");
    var jqxdata=require("jqxdata");
    var jqxbuttons=require("jqxbuttons");
    var jqxscrollbar=require("jqxscrollbar");
    var jqxdatatable=require("jqxdatatable");
    var jqxtreegrid=require("jqxtreegrid");

    var commonUtil = require("common");
    var customerId=commonUtil.getQuery("customerid");
    var layer=require("layer");
    var categoryModul={
        initSite:function(){
            $.ajax({
                url: "/category/getSiteList",
                data: {
                    customerId:customerId
                },
                async:false,
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if(data!=null){
                        if(data.data!=null&&data.data.length>0){
                            for(var i=0;i<data.data.length;i++){
                                $("#jq-cms-siteList").append("<option value='"+data.data[i].siteId+"'>"+data.data[i].name+"</option>")
                            }
                        }else{
                            switch (data.code){
                                case 200:
                                    $("#jq-cms-siteList").append("<option value='-1'>还没有站点信息</option>")
                                    break;
                                case 404:
                                    $("#jq-cms-siteList").append("<option value='-1'>还没有站点信息</option>")
                                    break;
                                case 502:
                                    layer.msg("服务器繁忙,加载站点信息失败",{time: 2000});
                                    break;
                            }
                        }
                    }
                    else{
                        layer.msg("服务器繁忙,加载站点信息失败",{time: 2000});
                        $("#jq-cms-siteList").append("<option value='-1'>还没有站点信息</option>")
                    }
                }
            });
        },
        initCategoryList:function(){
            var siteName=$("#jq-cms-siteList").find("option:selected").text();
            var employees=[{
                name: siteName+"(root)",
                id: -1,
                time: null,
                modelType: null,
                expanded: true,
                children:[]
            }];
            $.ajax({
                url: "/category/getCategoryList",
                data: {
                    siteId:$("#jq-cms-siteList").val(),
                    name: $("#categotyName").val()
                },
                async:false,
                type: "POST",
                dataType: 'json',
                success: function (data) {
                    if(data!=null&&data.data!=null&&data.data.length>0){
                        for (var i = 0; i < data.data.length; i++) {
                            employees[0].children.push(data.data[i]);
                        }
                    }
                }
            });
            return employees;
        },
        initList:function(){
            var source =
            {
                dataType: "json",
                dataFields: [
                    {name: 'children', type: 'array'},
                    {name: 'id', type: 'number'},
                    {name: 'name', type: 'string'},
                    {name: 'time', type: 'string'},
                    {name: 'orderWeight', type: 'number'},
                    {name: 'modelId', type: 'number'}
                ],
                hierarchy: {
                    root: 'children'
                },
                id: 'id',
                localData: categoryModul.initCategoryList()
            };
            var dataAdapter = new $.jqx.dataAdapter(source,{
                beforeLoadComplete: function (records) {
                    for (var i = 0; i < records.length; i++) {
                        records[i].expanded=true;
                    }
                }
                });
            var gridWidth = $(window).width() - 45;
            // create Tree Grid
            $("#treeGrid").jqxTreeGrid(
                {
                    width: $(window).width() - 45,
                    source: dataAdapter,
                    sortable: true,
                    columns: [
                        {text: '栏目名称', dataField: 'name', width: gridWidth * 0.25},
                        {text: '栏目ID', dataField: 'id', width: gridWidth * 0.15},
                        {
                            text: '所属模型',
                            dataField: 'modelId',
                            width: gridWidth * 0.15,
                            cellsRenderer: function (row, column, value) {
                                if (parseInt(value) >= 0) {
                                    switch (value) {
                                        case 0:
                                            return "文章模型";
                                        case 1:
                                            return "公告模型";
                                        case 2:
                                            return "视频模型";
                                        case 3:
                                            return "图库模型";
                                        case 4:
                                            return "下载模型";
                                        case 5:
                                            return "链接模型";
                                        default :
                                            return "自定义模型";
                                    }
                                } else {
                                    return "未设置模型";
                                }
                            }
                        },
                        {text: '排序权重', dataField: 'orderWeight', width: gridWidth * 0.15},
                        {
                            text: '创建时间',
                            dataField: 'time',
                            width: gridWidth * 0.15,
                            cellsRenderer: function (row, column, value) {
                                if (value) {
                                    return value.toString().substr(0, 10);
                                }
                            }
                        },
                        {
                            text: '操作',
                            cellsAlign: 'center',
                            align: "center",
                            columnType: 'none',
                            width: gridWidth * 0.15,
                            editable: false,
                            sortable: false,
                            dataField: null,
                            cellsRenderer: function (row, column, value) {
                                if (row == -1) {
                                    return "<a href='javascript:;' title='新增栏目' data-id='" + row + "' style='color:blue;margin-right:5px;' class='js-cms-addCategory'>新增栏目</a>|<a href='javascript:;' class='js-cms-updateCategory' data-id='" + row + "' title='修改栏目' style='color:#cccccc;margin-right:5px;margin-left: 5px;'>修改栏目</a>|<a href='javascript:;' title='删除' style='color:#cccccc;margin-left: 5px;'>删除</a>";
                                } else {
                                    var supper=$("#supper").val();
                                    if(supper=='True') {
                                        return "<a href='javascript:;' title='新增栏目' data-id='" + row + "' style='color:blue;margin-right:5px;' class='js-cms-addCategory'>新增栏目</a>|<a href='javascript:;' class='js-cms-updateCategory' data-id='" + row + "' title='修改栏目' style='color:blue;margin-right:5px;margin-left: 5px;'>修改栏目</a>|<a href='javascript:;' class='js-cms-categoryDelete' data-id='" + row + "' title='删除' style='color:blue;margin-left: 5px;'>删除</a>";
                                    }else{
                                        return "<a href='javascript:;' title='新增栏目' data-id='" + row + "' style='color:blue;margin-right:5px;' class='js-cms-addCategory'>新增栏目</a>|<a href='javascript:;' class='js-cms-updateCategory' data-id='" + row + "' title='修改栏目' style='color:blue;margin-right:5px;margin-left: 5px;'>修改栏目</a>|<a href='javascript:;'  data-id='" + row + "' title='删除' style='color:#cccccc;margin-left: 5px;'>删除</a>";
                                    }
                                }
                            }
                        }
                    ]
                });
            categoryModul.bindUpdateClick();
            $('#treeGrid').on('rowExpand', function (event) {//展开收缩事件
                categoryModul.bindUpdateClick();
            });
            $('#treeGrid').on('rowCollapse',function (event){
                categoryModul.bindUpdateClick();
             });
        },
        bindUpdateClick:function(){
            //新增栏目
            var obj=$(".js-cms-addCategory");
            $(".js-cms-addCategory").unbind("click");
            $(".js-cms-updateCategory").unbind("click");
            $(".js-cms-categoryDelete").unbind("click");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).attr('data-id');
                    categoryModul.openUpdateCategory(id,"新增栏目",1);
                })
            })
            //修改栏目
            var objUpdate=$(".js-cms-updateCategory");
            $.each(objUpdate,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).attr('data-id');
                    categoryModul.openUpdateCategory(id,"修改栏目",2);
                })
            })

            //删除事件绑定
            var objDelete=$(".js-cms-categoryDelete");
            $.each(objDelete,function(item,dom){
                $(dom).click(function(){
                    var id=$(dom).attr('data-id');
                    categoryModul.deleteCategory(id);
                })
            })
        },
        openUpdateCategory:function(id,title,type){
            var siteId=$("#jq-cms-siteList").val();
            var content='/category/addCategory/?id='+id+"&siteId="+siteId+"&customerid="+customerId //iframe的url
            if(type==2){
                content='/category/updateCategory/?id='+id+"&customerid="+customerId //iframe的url
            }
            layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: 0.8,
                area: ['900px', '500px'],
                content: content,
                end:function(){
                    categoryModul.initList();
                }
            });
        },
        deleteCategory:function(id){
            layer.confirm('您确定要删除该栏目？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/category/deleteCategory",
                    data: {
                        id: id
                    },
                    type: "POST",
                    dataType: 'json',
                    success: function (data) {
                        if(data!=null) {
                            var code = parseInt(data.code);
                            switch (code) {
                                case 200:
                                    layer.msg("删除成功",{time: 2000});
                                    categoryModul.initList();
                                    break;
                                case 500:
                                    layer.msg("删除失败",{time: 2000});
                                    break;
                                case 202:
                                    layer.msg("对不起,您没有删除权限",{time: 2000});
                                    break;
                                case 502:
                                    layer.msg("系统繁忙,请稍后再试...",{time: 2000});
                                    break;
                            }
                        }
                    },
                    error:function(){
                        layer.msg("网络出现异常,请稍后再试...",{time: 2000});
                    }
                });
            });
        }
    };
    categoryModul.initSite();//加载站点列表信息
    setTimeout(categoryModul.initList,500);//延时500毫秒加载,解决初次加载加载js出错问题
    //initList();//加载栏目列表信息
    $("#jq-cms-siteList").on("change",function(){
        categoryModul.initList();
    })
    $("#jq-cms-search").on("click",function(){
        categoryModul.initList();
    })
});