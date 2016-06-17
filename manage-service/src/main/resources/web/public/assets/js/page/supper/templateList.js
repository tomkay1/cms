/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var layer=require("layer");
    var ownerId =commonUtil.getQuery("ownerId");
    var SiteGrid=$("#js-SiteList").Grid({
        method: 'POST',//提交方式GET|POST
        form: 'form1',//表单ID
        pageSize: 10,
        dataParam:{
            ownerId:ownerId
        },
        height:'auto',
        showNumber: false,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        url: '/manage/supper/getTemplateList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '10%', field: 'tempName', title: '模板名称', align: 'center'},
            {width: '20%', field: 'thumbUri', title: '缩略图', align: 'center',
                formatter:function(value,rowData){
                    if(value!=null){
                        return "<img src=/"+rowData.thumbUri+" style='width:100%;height:300px;'/>";
                    }
                    return "";
                }

            },
            {width: '10%' , field: 'previewTimes',title: '预览量',align: 'center'},
            {width: '10%' , field: 'scans',title: '浏览量',align: 'center'},
            {width: '10%' , field: 'lauds',title: '点赞数量',align: 'center'},
            {
                width: '20%', field: 'createTime', title: '创建时间', align: 'center',
                formatter: function (value, rowData) {
                    if(value!=null)
                    {
                        return value.year+"-"+value.monthValue+"-"+value.dayOfMonth+" "+value.hour+":"+value.minute;
                    }
                    return "";
                }
            },
            {width: '20%', field: 'title', title: '操作', align: 'center',
                formatter: function (value, rowData) {
                    return "<a href='javascript:' class='js-hot-templateDelete' data-id='"+rowData.id+"' style='margin-right:10px; color:blue;'>删除</a>" +
                        "<a href='javascript:' class='js-hot-templateConfig' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>更多配置</a>"+
                        "<a href='javascript:' class='js-hot-templateUpdate' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>修改</a>" +
                        "<a href='javascript:' class='js-hot-templateUse' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>使用</a>" +
                        "<a href='javascript:' class='js-hot-templateView' data-id='"+rowData.id+"' style='margin-right:10px; color: blue'>预览</a>"
                }
            }
        ]
    },function(){
        updateTemplate();
        deleteTemplate();
        openConfig();
        useTemplate();
        viewTemplate();
    });
   //搜索
    $("#jq-cms-search").click(function(){
        var commonUtil = require("common");
        commonUtil.setDisabled("jq-cms-Save");
        var ownerId =commonUtil.getQuery("ownerId");
        var option={
            dataParam:{
                name:$("#siteName").val(),
                ownerId:ownerId
            }
        };
        SiteGrid.Refresh(option);
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
        SiteGrid.Refresh(option);
    })

    //修改
    function updateTemplate(){
        var obj=$(".js-hot-templateUpdate");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                window.location.href="http://"+window.location.host+"/"+"supper/updateSite?id="+id+"&ownerId="+ownerId;
            })
        })
    }

    //使用
    function useTemplate(){
        var obj=$(".js-hot-templateUse");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                window.location.href="http://"+window.location.host+"/"+"template/use?templateId="+id+"&siteId=69";
            })
        })
    }
    //预览
    function viewTemplate(){
        var obj=$(".js-hot-templateView");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定修改事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var urlRoot="";
                $.ajax({
                    url:"/manage/template/view",
                    data:{
                        templateId:id
                    },
                    dataType:"json",
                    success:function(data){
                        if(data!=null){
                            var combineData=data.data;
                            var url=combineData.split(",")[0];
                            var id=combineData.split(",")[1];
                            var debug=1;//debug模式，线上要删除
                            if(id!=-1){
                                if(debug==1)//如果ID=-1,表明该站点还没有一个主页面
                                    urlRoot=url+":8080/front/shop/"+id;
                                else
                                    urlRoot=url+"/shop"+id;
                                window.location.href=urlRoot;
                            }else{
                                layer.msg("当前模板还不能预览，请联系网站开发商",{time: 2000});
                            }
                        }else{
                            layer.msg("当前模板还不能预览，请联系网站开发商",{time: 2000});
                        }
                    }
                })
                //var commonUtil = require("common");
                //var ownerId = commonUtil.getQuery("ownerId");
                //window.location.href="http://"+window.location.host+"/"+"template/web.private.view?templateId="+id;
            })
        })
    }

    function view(){
        $.ajax({
            url:"/manage/template/view",

        })
    }


    //删除
    function deleteTemplate(){
        var obj=$(".js-hot-templateDelete");
        $.each(obj,function(item,dom){
            $(dom).click(function(){//绑定删除事件
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                layer.confirm('您确定要删除该站点吗？', {
                    btn: ['确定','取消'] //按钮
                }, function() {
                    $.ajax({
                        url: "/manage/supper/deleteSite",
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
                                        SiteGrid.reLoad();
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

    function openConfig(){
        var obj=$(".js-hot-siteConfig");
        $.each(obj,function(item,dom){
            $(dom).click(function(){
                var id=$(this).attr("data-id");//Html5可以使用$(this).data('id')方式来写;
                var commonUtil = require("common");
                var ownerId = commonUtil.getQuery("ownerId");
                layer.open({
                    type: 2,
                    title: "修改配置信息",
                    shadeClose: true,
                    shade: 0.8,
                    area: ['500px', '300px'],
                    content: "/manage/supper/siteConfig?siteId="+id+"&ownerId="+ownerId,
                    end:function(){
                        //SiteGrid.Refresh();
                    }
                });
            });
        })
    }
});