/**
 * 商城页面效果入口
 * */
define(function (require, exports, module) {
    var widgetEffect={
        widgetTest:function(){
            window.console.log("测试");
        },
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
                var widget = require("advanceSearch");
                widget.init();
            }
        },
        binner:function(){
            var obj = $(".js-binner");
            if(obj.length > 0){
                var sliber=require("superSlide");
                window.console.log(sliber);
                var widget = require("binner");
                widget.init();
            }
        },
        widgetInit:function(){
            widgetEffect.widgetTest();
            widgetEffect.classification();
            widgetEffect.goodList();
            widgetEffect.advanceSearch();
            widgetEffect.binner();
        }
    }

    widgetEffect.widgetInit();
});