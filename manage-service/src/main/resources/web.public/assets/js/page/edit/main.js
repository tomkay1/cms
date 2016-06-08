/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.calcuHeight("mainpanel");
    commonUtil.calcuHeightToTop("menuContainer",74);
    commonUtil.cacleHeightByIframe("content",74);
});