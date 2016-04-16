/**
 * Created by admin on 2016/4/13.
 */
define(function(require, exports, module){
    return{
        tabClick: function(){
            var obj = $(".jTab a");
            $.each(obj, function (item, dom) {
                $(dom).click(function(){
                    var sortColumn = $(dom).attr("sortColumn");
                    var sort = $("input[class~=js-sort]");
                    if(sort.val().indexOf(sortColumn) <= -1){
                        obj.removeAttr("sortDirect");
                        obj.removeClass("current");
                        $(dom).addClass("current");
                    }
                    var sortDirect = $(dom).attr("sortDirect");
                    if(typeof (sortDirect) == 'undefined'){
                        sortDirect = 'desc';
                        $(dom).attr("sortDirect",'desc');
                    }else if('desc'.indexOf(sortDirect) > -1){
                        sortDirect = "asc";
                    }else if("asc".indexOf(sortDirect) > -1){
                        sortDirect = "desc";
                    }
                    sort.val(sortColumn + "," + sortDirect);
                    $("input[class~=current-index]").val(0);
                    $("form[class~=js-advance-search]").submit();
                })
            });
        },
        pageNoClick:function(pageNo){
            var obj = $(".jPage a");
            $.each(obj, function (item, dom) {
                $(dom).click(function(){
                    console.log();
                    if(typeof ($(dom).attr("pageNo")) != 'undefined'){
                        var pageNo = $(dom).attr("pageNo");
                        $("input[class~=current-index]").val(pageNo);
                    }
                    $("form[class~=js-advance-search]").submit();
                })
            });
        },
        init:function(){
            this.tabClick();
            this.pageNoClick();
        }
    }
});
