/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.calcuHeight("mainpanel");
    commonUtil.calcuHeightToTop("menuContainer",74);
    commonUtil.calcuHeightToTop("con_right",74);
    commonUtil.calcuHeightToTop("con_left",$("#con_left").offset().top);
    commonUtil.calcuWidth("content",$(".leftpanel").width());
    commonUtil.cacleHeightByIframe("content",74);
    commonUtil.calcuWidth("con_right",$(".leftpanel").width());

    //左边栏目效果
    var menusObj=$(".js-cms-menus");
    $.each(menusObj,function(item,dom){
        $(dom).find(".aparent").click(function(){
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

    var subMenusObj=$(".js-cms-submunes");
    $.each(subMenusObj,function(item,dom){
        $(dom).click(function(){
            $(".js-cms-submunes").removeClass("active");
            $(".js-cms-menus").removeClass("active")
            $(dom).addClass("active");
            var parentId=$(dom).data('id');
            $("#"+parentId).addClass("active");
        })
    })
});