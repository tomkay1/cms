/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $(".select1").uedSelect({
        width: 150
    });

    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var scope=commonUtil.getQuery("scope");
    var ownerId =commonUtil.getQuery("ownerId");

    var obj=$(".js-cms-defaults");
    $.each(obj,function(item,dom){
        $(dom).click(function(){
            var ownerId=$(dom).data("ownerId");
            var urlFormatter=$(dom).data("url")+"&scope="+scope;
            var siteId=$("#siteId").val();
            var url=commonUtil.formatString(urlFormatter,ownerId,siteId);
            window.location.href=url;
        })
    })
});