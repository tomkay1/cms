/**
 * Created by admin on 2016/4/13.
 */
define(["js/jquery.SuperSlide.2.1.1"],function(require, exports, module){
    return{
        init:function(){
            $(".fullSlide").slide({
                titCell:".hd ul",
                mainCell:".bd ul",
                effect:"fold",
                autoPlay:true,
                autoPage:true,
                trigger:"click"
            });
        }
    }
});
