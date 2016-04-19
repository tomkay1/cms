/**
 * Created by admin on 2016/4/13.
 */
define(function (require, exports, module) {
    return {
        tabClick: function () {
            var obj = $(".HOTTab a");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    //get guid
                    var guid = $(dom).parent().attr("guid");
                    if (typeof (guid) != 'undefined') {
                        var sortColumn = $(dom).attr("sortColumn");
                        var sort = $("div[id=" + guid + "] input[class~=js-sort]");
                        if (sort.val().indexOf(sortColumn) <= -1) {
                            obj.removeAttr("sortDirect");
                            obj.removeClass("current");
                            $(dom).addClass("current");
                        }
                        var sortDirect = $(dom).attr("sortDirect");
                        if (typeof (sortDirect) == 'undefined') {
                            sortDirect = 'desc';
                            $(dom).attr("sortDirect", 'desc');
                        } else if ('desc'.indexOf(sortDirect) > -1) {
                            sortDirect = "asc";
                        } else if ("asc".indexOf(sortDirect) > -1) {
                            sortDirect = "desc";
                        }
                        sort.val(sortColumn + "," + sortDirect);
                        $("div[id=" + guid + "] input[class~=current-index]").val(0);
                        $("form[class~=js-form-" + guid + "]").submit();
                    }
                })
            });
        },
        pageNoClick: function (pageNo) {
            var obj = $(".HOTPage a");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    //get guid
                    var guid = $(dom).parent().attr("guid");
                    if (typeof (guid) != 'undefined') {
                        if (typeof ($(dom).attr("pageNo")) != 'undefined') {
                            var pageNo = $(dom).attr("pageNo");
                            $("div[id=" + guid + "] input[class~=current-index]").val(pageNo);
                        }
                        $("form[class~=js-form-" + guid + "]").submit();
                    }
                })
            });
        },
        topClassClick: function () {
            var obj = $(".js-advance-search .js-top-category a");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var guid = $(dom).parent().attr("guid");
                    if (typeof (guid) != 'undefined') {
                        if ($(dom).hasClass("on")) {
                            $("div[id=" + guid + "] input[class~=js-goodsCatId]").val('');
                            $("form[class~=js-form-" + guid + "]").submit();
                        } else {
                            $("div[id=" + guid + "] input[class~=js-goodsCatId]").val($(dom).attr("catId"));
                            $("form[class~=js-form-" + guid + "]").submit();
                        }
                    }
                })
            });
        },
        priceClassClick: function () {
            var obj = $(".js-advance-search-price a");
            $.each(obj, function (item, dom) {
                $(dom).click(function () {
                    var guid = $(dom).parent().attr("guid");
                    if (typeof (guid) != 'undefined') {
                        if ($(dom).hasClass("on")) {
                            $("input[class~=js-minPrice]").val('');
                            $("input[class~=js-maxPrice]").val('');
                            $("form[class~=js-advance-search]").submit();
                        } else {
                            $("input[class~=js-minPrice]").val($(dom).attr('minPrice'));
                            $("input[class~=js-maxPrice]").val($(dom).attr('maxPrice'));
                            $("form[class~=js-advance-search]").submit();
                        }
                    }
                })
            });
        },
        init: function () {
            this.tabClick();
            this.pageNoClick();
            this.topClassClick();
            this.priceClassClick();
        }
    }
});
