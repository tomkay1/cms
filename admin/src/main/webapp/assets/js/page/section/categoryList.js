/**
 * Created by xhl on 2015/12/30.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    var customerId=commonUtil.getQuery("customerid");
    var layer=require("layer");
    function initSite(){
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
    }
    function initCategoryList(){
        var employees=[];
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
                        employees.push(data.data[i]);
                    }
                }else {
                    //var siteName=$("#jq-cms-siteList").select
                    var siteName=$("#jq-cms-siteList").find("option:selected").text();
                    if ($("#jq-cms-siteList").val() != "-1") {
                        employees.push({
                            name: siteName+"(root)",
                            id: -1,
                            time: null,
                            modelType: null
                        });
                    }
                }
            }
        });
        return employees;
    }
    function initList(){
        var source =
        {
            dataType: "json",
            dataFields: [
                { name: 'children', type: 'array' },
                { name: 'id', type: 'number' },
                { name: 'name', type: 'string' },
                { name: 'time', type: 'string' },
                { name: 'modelType', type: 'number' }
            ],
            hierarchy:
            {
                root: 'children'
            },
            id: 'id',
            localData: initCategoryList()
        };
        var dataAdapter = new $.jqx.dataAdapter(source);
        var gridWidth=$(window).width()-45;
        // create Tree Grid
        $("#treeGrid").jqxTreeGrid(
            {
                width: $(window).width()-45,
                source: dataAdapter,
                sortable: true,
                columns: [
                    { text: '栏目名称', dataField: 'name', width: gridWidth*0.25 },
                    { text: '所属模型', dataField: 'modelType', width: gridWidth*0.25,cellsRenderer: function (row, column, value) {
                        if(parseInt(value)>=0){
                            switch (value){
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
                        }else{
                            return "未设置模型";
                        }
                    }},
                    { text: '创建时间', dataField: 'time', width: gridWidth*0.25,cellsRenderer:function(row, column, value){
                        if(value){
                            return value.toString().substr(0,10);
                        }
                    } },
                    {
                        text: '操作', cellsAlign: 'center', align: "center", columnType: 'none',width: gridWidth*0.25, editable: false, sortable: false, dataField: null, cellsRenderer: function (row, column, value) {
                        if(row==-1){
                            return "<a href='#' title='新增栏目' data-id='"+row+"' style='color:blue;margin-right:5px;' class='js-cms-addCategory'>新增栏目</a>|<a href='#' class='js-cms-updateCategory' data-id='"+row+"' title='修改栏目' style='color:#cccccc;margin-right:5px;margin-left: 5px;'>修改栏目</a>|<a href='#' title='删除' style='color:#cccccc;margin-left: 5px;'>删除</a>";
                        }else{
                            return "<a href='#' title='新增栏目' data-id='"+row+"' style='color:blue;margin-right:5px;' class='js-cms-addCategory'>新增栏目</a>|<a href='#' class='js-cms-updateCategory' data-id='"+row+"' title='修改栏目' style='color:blue;margin-right:5px;margin-left: 5px;'>修改栏目</a>|<a href='#' title='删除' style='color:blue;margin-left: 5px;'>删除</a>";
                        }
                    }
                    }
                ]
            });
        bindUpdateClick();
        $('#treeGrid').on('rowExpand',function (event){//展开收缩事件
                bindUpdateClick();
            });
    }
    function bindUpdateClick(){
        //新增栏目
        var obj=$(".js-cms-addCategory");
        $(".js-cms-addCategory").unbind("click");
        $(".js-cms-updateCategory").unbind("click");
        $.each(obj,function(item,dom){
            $(dom).click(function(){
                var id=$(dom).attr('data-id');
                openUpdateCategory(id,"新增栏目");
            })
        })
        //修改栏目
        var objUpdate=$(".js-cms-updateCategory");
        $.each(objUpdate,function(item,dom){
            $(dom).click(function(){
                var id=$(dom).attr('data-id');
                openUpdateCategory(id,"修改栏目");
            })
        })
    }
    initSite();//加载站点列表信息
    setTimeout(initList,500);//延时500毫秒加载,解决初次加载加载js出错问题
    //initList();//加载栏目列表信息
    $("#jq-cms-siteList").on("change",function(){
        initList();
    })
    function openUpdateCategory(id,title){
        var siteId=$("#jq-cms-siteList").val();
        layer.open({
            type: 2,
            title: title,
            shadeClose: true,
            shade: 0.8,
            area: ['900px', '500px'],
            content: '/category/addCategory/?id='+id+"&customerid="+customerId //iframe的url
    });
    }
});