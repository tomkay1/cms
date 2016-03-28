define(function (require, exports, module) {
    var widget={
        widgetTab:function(){
            var obj=$(".js-widget-module");
            $.each(obj,function(item,dom){
                $(dom).click(function(){
                    var cateId=$(dom).data("cateid");
                    $(".j-module-list ul").removeClass("show");
                    $(".js-widget-module").removeClass("show");
                    $("#my_modules_"+cateId).addClass("show");
                    $(dom).addClass("show");
                });
            })
        }
    }
    widget.widgetTab();
});