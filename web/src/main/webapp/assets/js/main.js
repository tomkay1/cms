/**
 * 商城页面效果入口
 * */
define(function (require, exports, module) {
    var widgetEffect={
        widgetTest:function(){
            window.console.log("测试");
        },
        widgetInit:function(){
            widgetEffect.widgetTest();
        }
    }

    widgetEffect.widgetInit();
});