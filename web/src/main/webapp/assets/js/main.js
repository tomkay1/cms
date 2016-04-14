/**
 * 商城页面效果入口
 * */
define(function (require, exports, module) {
    var widgetEffect={
        widgetTest:function(){
            window.console.log("测试");
        },
        classification:function(){
            var obj=$(".js-top-category");
            if(obj.length > 0){
                var widget=require("classification");
                widget.topClassClick();
            }
        },
        widgetInit:function(){
            widgetEffect.widgetTest();
            widgetEffect.classification();
        }
    }

    widgetEffect.widgetInit();
});