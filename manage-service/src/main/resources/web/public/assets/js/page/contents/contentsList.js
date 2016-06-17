/**
 * Created by chendeyu on 2015/12/24.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var commonCategory=require("categoryCommon");
    var ownerId =commonUtil.getQuery("ownerId");
    var siteId = $("#siteId").val();
    commonCategory.getCategoryList(siteId,"","category");
    var category = $("#category").val();
    var ContentsGrid=$("#js-ContentsList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        dataParam:{
            ownerId:ownerId,
            siteId:siteId,
            category:category,
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/contents/getContentsList',//数据来源Url|
        rows: [
            {width: '10%', field: 'id', title: '内容ID', align: 'center'},
            {width: '15%', field: 'name', title: '所属栏目', align: 'center'},
            {width: '10%', field: 'modelname', title: '所属模型', align: 'center'},
            {width: '15%', field: 'title', title: '标题', align: 'center'},
            {width: '20%', field: 'description', title: '描述', align: 'center'},
            {
                width: '10%', field: 'createTime', title: '创建时间', align: 'center',
            }
            ,
            {width: '10%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    if(rowData.model=="gallery"){
                        return    "<a href='javascript:' class='js-hot-contentsDelete' data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-contentsUpdate'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"+
                        "<a href='javascript:' class='js-hot-contentsList'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>查看详情</a>"
                        + "<a href='javascript:' class='js-hot-addGalleryList'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>添加图片</a>"+
                        "<a href='javascript:' class='js-hot-galleryListDetail'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>查看图库</a>"
                    }else{
                        return     "<a href='javascript:' class='js-hot-contentsDelete' data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-contentsUpdate'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>"+
                        "<a href='javascript:' class='js-hot-contentsList'data-model='"+rowData.model+"' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>查看详情</a>"
                    }
                }
            }
        ]
    },function(){
        updateContents();
        deleteContents();
        contentsList();
        addGalleryList();
        galleryList();
    });

    function search(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        var option={
            dataParam:{
                siteId:$("#siteId").val(),
                category:$("#category").val(),
                name:$("#contentsName").val(),
                ownerId:ownerId
            }
        };
        ContentsGrid.Refresh(option);
    }
//搜索
    $("#jq-cms-search").click(function(){
        search();
    })

    $("#jq-cms-add").click(function(){
        var commonUtil = require("common");
        var ownerId =commonUtil.getQuery("ownerId");
        var siteId=$("#siteId").val();
        var category=$("#category").val();
        var layer=require("layer");
        //window.location.href="http://"+window.location.host+"/contents/addContents"+"?siteId="+siteId+"&ownerId="+ownerId+"&category="+category;
        var content="/contents/addContents"+"?siteId="+siteId+"&ownerId="+ownerId+"&category="+category;
        layer.open({
            type: 2,
            title: "添加内容",
            shadeClose: true,
            shade: 0.8,
            area: ['1000px', '500px'],
            content: content,
            end:function(){
                ContentsGrid.Refresh();
            }
        });
    })

    //显示所有
    $("#jq-cms-searchAll").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        var option={
            dataParam:{
                name:"",
                ownerId:ownerId
            }
        };
        ContentsGrid.Refresh(option);
    })

    //修改
    function updateContents(){
        var obj=$(".js-hot-contentsUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var model=$(this).attr("data-model");//Html5可以使用$(this).data('id')方式来写;
                var arrayText =model.split("");//article->Article
                var layer=require("layer");
                var link="";
                for(var i=0;i<arrayText.length;i++){
                    if(i==0){
                        link =link+""+arrayText[i].toUpperCase();
                    }
                    else{
                        link =link+arrayText[i];
                    }
                }
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                //window.location.href="http://"+window.location.host+"/"+model+"/update"+link+"?id="+id+"&ownerId="+ownerId;
                var content ="/"+model+"/update"+link+"?id="+id+"&ownerId="+ownerId;
                layer.open({
                    type: 2,
                    title: "修改内容",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['1000px', '700px'],
                    content: content,
                    end:function(){
                        ContentsGrid.Refresh();
                    }
                });
            })
        })
    }


    //删除
    function deleteContents(){
        var obj=$(".js-hot-contentsDelete");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var model=$(this).attr("data-model");//Html5可以使用$(this).data('id')方式来写;
                var arrayText =model.split("");//article->Article
                var link="";
                for(var i=0;i<arrayText.length;i++){
                    if(i==0){
                        link =link+""+arrayText[i].toUpperCase();
                    }
                    else{
                        link =link+arrayText[i];
                    }
                }
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                var layer=require("layer");
                layer.confirm('您确定要删除该内容吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/"+model+"/delete"+link,
                        data: {
                            id:id,
                            ownerId:ownerId
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
                                        ContentsGrid.reLoad();
                                        break;
                                    case 202:
                                        layer.msg("对不起,您没有删除权限",{time: 2000});
                                        break;
                                    case 206:
                                        layer.msg("对不起,系统文章不可做删除操作",{time: 2000});
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

    //查看详情
    function contentsList(){
        var obj=$(".js-hot-contentsList");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var model=$(this).attr("data-model");//Html5可以使用$(this).data('id')方式来写;
                model=$.trim(model);
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                //window.location.href="http://"+window.location.host+"/"+model+"/"+model+"List"+"?id="+id+"&ownerId="+ownerId;
                var content="/"+model+"/"+model+"List"+"?id="+id+"&ownerId="+ownerId;
                var layer=require("layer");
                layer.open({
                    type: 2,
                    title: "内容详情",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['1000px', '700px'],
                    content: content,
                    end:function(){
                        ContentsGrid.Refresh();
                    }
                });
        })
    })
    }

    //添加图片
    function addGalleryList(){
        var obj=$(".js-hot-addGalleryList");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var content="/gallery/addGalleryList"+"?id="+id+"&ownerId="+ownerId;
                var layer=require("layer");
                layer.open({
                    type: 2,
                    title: "添加图片",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['1000px', '700px'],
                    content: content,
                    end:function(){
                        ContentsGrid.Refresh();
                    }
                });
            })
        })
    }

    //查看图库列表
    function galleryList(){
        var obj=$(".js-hot-galleryListDetail");

        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var model=$(this).attr("data-model");//Html5可以使用$(this).data('id')方式来写;
                model=$.trim(model);
                var content="/gallery/galleryListDetail"+"?id="+id+"&ownerId="+ownerId;
                var layer=require("layer");
                layer.open({
                    type: 2,
                    title: "图库列表",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['1000px', '700px'],
                    content: content,
                    end:function(){
                        ContentsGrid.Refresh();
                    }
                });
            })
        })
    }




    $("#siteId").bind("change",function(){
        //setSecond(this);
        search();
        siteId= $("#siteId").val();
        commonCategory.getCategoryList(siteId,"","category");
    })
    $("#category").bind("change",function(){
        search();
    })
    //当改变站点时，栏目产生变化
    function setSecond(obj){
        var siteId = obj.value;
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        $.ajax({
            url: "/contents/contentsSelect",
            dataType: 'json',
            type: "GET",
            data: {
                ownerId:ownerId,
                siteId :siteId
            },
            error: function (data, status, e) {

            },
            success: function (data) {
                var category =document.getElementById('category');
                category.innerHTML="<option value="+-1+ ">" +"请选择"+"</option>";
                for(var i=0;i<data.length;i++){
                    option_first = document.createElement('OPTION');
                    option_first.setAttribute('value',data[i].categoryId );
                    option_first.innerHTML = data[i].categoryName;
                    category.appendChild(option_first);
                }

            }
        });
    }
});

