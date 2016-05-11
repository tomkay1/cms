/**
 * 商城页面效果入口
 * */
define(function (require, exports, module) {
    var widgetEffect={
        classification:function(){
            var obj=$(".js-classification");
            if(obj.length > 0){
                var widget=require("classification");
                widget.init();
            }
        },
        goodList:function(){
            var obj = $(".js-good-list");
            if(obj.length > 0){
                var widget = require("goodList");
                widget.init();
            }
        },
        advanceSearch:function(){
            var obj = $(".js-advance-search");
            if(obj.length > 0){
                console.log("advance-search");
                var widget = require("advanceSearch");
                widget.init();
            }
        },
        banner:function(){
            var obj = $(".js-banner");
            if(obj.length > 0){
                require("superSlide");
                var widget = require("banner");
                widget.init();
            }
        },
        salesRanking:function(){
            var obj = $(".js-sales-ranking");
            if(obj.length > 0){
                var widget = require("salesRanking");
                widget.init();
            }
        },
        searchSubmit:function(){
            var obj=$(".js-search-button");
            if(obj.length>0){
                var widget=require("searchSubmit")
                widget.init();
            }
        },
        widgetInit:function(){
            //widgetEffect.widgetTest();
            widgetEffect.classification();
            widgetEffect.goodList();
            widgetEffect.advanceSearch();
            widgetEffect.banner();
            widgetEffect.salesRanking();
            widgetEffect.searchSubmit();
        }
    }

    widgetEffect.widgetInit();
});