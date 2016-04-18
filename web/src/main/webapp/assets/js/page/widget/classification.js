/**
 * Created by admin on 2016/4/13.
 */
define(function (require, exports, module) {
    return {
        topClassClick: function () {
            var obj = $(".js-classification .js-top-category");
            var guid = obj.parent().attr("guid");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var catId = $(dom).attr("catId");
                    var parentDiv = $("div[class~='jTwoLevel jNoBorderBottm'][catId=" + catId + "]");
                    if (!parentDiv.hasClass("current")) {
                        parentDiv.addClass("current");
                        parentDiv.next().attr("style", "display:none");
                    } else {
                        parentDiv.removeClass("current");
                        parentDiv.next().attr("style", "display:block");
                    }
                })
            });
            if (typeof (guid) != 'undefined') {
                var subObj = $("div[id=" + guid + "] .js-sub-category a");
                $.each(subObj, function (item, dom) {
                    $(dom).click(function () {
                        subObj.removeClass("on");
                        $(dom).addClass("on");
                    })
                });
            }
        },
        getClickCategory: function () {
            var common = require("common");
            var goodsCatId = common.getQuery("goodsCatId");
            if (goodsCatId.length > 0) {
                $(".js-classification .js-" + goodsCatId).parent().attr("style", "display:block");
                $(".js-classification .js-" + goodsCatId).parent().prev().removeClass("current");
                $(".js-classification .js-" + goodsCatId).find("a").addClass("on");
            }
        },
        init: function () {
            this.topClassClick();
            this.getClickCategory();
        }
    }
});
