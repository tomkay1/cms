/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    $(".select1").uedSelect({
        width: 150
    });

    var commonUtil = require("common");
    commonUtil.setDisabled("jq-cms-Save");
    var customerId =commonUtil.getQuery("customerId");

    var obj=$(".js-cms-defaults");
    $.each(obj,function(item,dom){
        $(dom).click(function(){
            var customerId=$(dom).data("customerid");
            var urlFormatter=$(dom).data("url");
            var siteId=$("#siteId").val();
            var url=commonUtil.formatString(urlFormatter,customerId,siteId);
            window.location.href=url;
        })
    })
});