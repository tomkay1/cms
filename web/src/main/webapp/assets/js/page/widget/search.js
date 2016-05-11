/**
 * 页面搜索框去空格处理
 */
define(function (require, exports, module) {
    return {
        init: function () {
            $(".js-search-button").on('click',function(){
                $(".js-search-keyword").val($.trim($(".js-search-keyword").val()));
                $(this).parent(".js-search-form").submit();
            })
        }
    }
});
