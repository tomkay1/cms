/**
 * Created by admin on 2016/4/13.
 */
define(function (require, exports, module) {
    return {
        init: function () {
            $(".js-sales-ranking ul li").hover(function(){
                $(this).addClass("HOTCurrent");
            },function(){
                $(this).removeClass("HOTCurrent");
            })
        }
    }
});
