/**
 * 页面搜索框去空格处理
 */
define(function (require, exports, module) {
    return {
        init: function () {
            console.log(11);
            $(".js-search-button").on('click',function(){
                console.log(22);
                $(this).siblings(".js-search-keyword").val($.trim($(this).siblings(".js-search-keyword").val()));
                $(this).parent(".js-search-form").submit();
            })
        }
    }
});
