/**
 * Created by Administrator on 2015/12/21.
 */
define(["js/jquery-1.9.1.min"],function () {
    return {
        calcuHeight:function(id){
            var height = $(document).height();
            $("#"+id).height(height);
        },
        calcuHeightToTop:function(id,topHeight)
        {
            var height = $(document).height()-topHeight;
            $("#"+id).height(height);
        },
        calcuWidth:function(id,leftWidth){
            var win = $(window);
            var width = win.width() - leftWidth;
            $("#"+id).width(width);
        },
        setDisabled: function (id) {
            var disabledText = $("#" + id).attr("data-submiting-text");
            $("#" + id).addClass("disabled");
            $("#" + id).html(disabledText);
        },
        cancelDisabled: function (id) {
            //setTimeout(function () {
            var abledText = $("#" + id).attr("data-text");
            $("#" + id).removeClass("disabled");
            $("#" + id).html(abledText);
            //}, 2000);
        }
    }
});