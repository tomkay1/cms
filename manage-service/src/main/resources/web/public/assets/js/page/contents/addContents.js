/**
 * Created by chendeyu on 2016/1/11.
 */
define(function (require, exports, module) {
    window.onload=function(){
        //var modelId= $("#categoryId").find("option:selected").attr("data-modelType");
        //var commonUtil = require("common");
        //commonUtil.setDisabled("jq-cms-Save");
        //var ownerId =commonUtil.getQuery("ownerId");
        //var widgetUrl="";
        //if(modelId==0) {
        //    widgetUrl = "/article/addArticle?ownerId=" + ownerId;
        //}
        //if (modelId == 1) {
        //    widgetUrl = "/notice/addNotice?ownerId=" + ownerId;
        //}
        //if (modelId == 2) {
        //    widgetUrl = "/video/addVideo?ownerId=" + ownerId;
        //}
        //if (modelId == 3) {
        //    widgetUrl = "/gallery/addGallery?ownerId=" + ownerId;
        //}
        //if (modelId == 4) {
        //    widgetUrl = "/download/addDownload?ownerId=" + ownerId;
        //}
        //if (modelId == 5) {
        //    widgetUrl = "/link/addLink?ownerId=" + ownerId;
        //}
        //$.get(widgetUrl+"&_="+Math.random(), function (html) {
        //    $("#widget").html(html);
        //});
        initViewModule.initView();
    }

    $("#categoryId").on("change",function(){
        initViewModule.initView();
    })

    var initViewModule={
        initView:function(){
            $(".jq-jupload-box").remove();
            var modelId= $("#categoryId").find("option:selected").attr("data-modelType");
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            var ownerId =commonUtil.getQuery("ownerId");
            var widgetUrl="";
            if(modelId==0) {
                widgetUrl = "/article/addArticle?ownerId=" + ownerId;
            }
            if (modelId == 1) {
                widgetUrl = "/notice/addNotice?ownerId=" + ownerId;
            }
            if (modelId == 2) {
                widgetUrl = "/video/addVideo?ownerId=" + ownerId;
            }
            if (modelId == 3) {
                widgetUrl = "/gallery/addGallery?ownerId=" + ownerId;
            }
            if (modelId == 4) {
                widgetUrl = "/download/addDownload?ownerId=" + ownerId;
            }
            if (modelId == 5) {
                widgetUrl = "/link/addLink?ownerId=" + ownerId;
            }
            if(modelId!=-2) {
                $.get(widgetUrl + "&_=" + Math.random(), function (html) {
                    $("#widget").html(html);
                });
            }
        }
    }

    initViewModule.initView();

    $("#jq-cms-return").click(function(){
        var layer=require("layer");
        var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(layerIndex);
    })


});
