/**
 * Created by admin on 2016/4/13.
 */
define(function(require, exports, module){
    return{
        init:function(){
            //var obj=$(".js-banner");
            //$.each(obj,function(item,dom){
            //    window.console.log($(".fullSlide"));
                $(".fullSlide").slide({
                    titCell:".hd ul",
                    mainCell:".bd ul",
                    effect:"fold",
                    autoPlay:true,
                    autoPage:true,
                    trigger:"click"
                });
            //})
        }
    }
});
