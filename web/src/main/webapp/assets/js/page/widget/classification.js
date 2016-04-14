/**
 * Created by admin on 2016/4/13.
 */
define(function(require, exports, module){
    return{
        topClassClick: function(){
            var obj = $(".js-top-category");
            $.each(obj, function (item, dom) {
                $(dom).click(function(){
                    var p_class = $(dom).parent().attr("class");
                    if(p_class == 'jTwoLevel jNoBorderBottm'){
                        $(dom).parent().attr("class","jTwoLevel jNoBorderBottm current");
                        $(dom).parent().parent().find('.js-sub-category').attr("style","display:none");
                    }else{
                        $(dom).parent().attr("class","jTwoLevel jNoBorderBottm");
                        $(dom).parent().parent().find('.js-sub-category').attr("style","display:block");
                    }
                })
            });
        }
    }
});
