/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.calcuHeight("mainpanel");

    //TODO:左边栏目效果
    var menusObj=$(".js-cms-menus");
    $.each(menusObj,function(item,dom){
        $(dom).click(function(){
            if($(dom).find("ul").hasClass('hidden'))
            {
                $(dom).removeClass("nav-active")
                $(dom).find("ul").removeClass("hidden");
                //$(dom).find("ul").addClass("hidden")
            }
            else
            {
                //$(dom).find("ul").removeClass("hidden")
                $(dom).addClass("nav-active");
                $(dom).find("ul").addClass("hidden");
            }
        });
    })
});