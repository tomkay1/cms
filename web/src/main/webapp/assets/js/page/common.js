/**
 * Created by Administrator on 2015/12/21.
 */
define(["js/jquery-1.9.1.min"],function () {
    return {
        /**
        * @brief 获得页面参数
        * @param 参数名
        * */
        getQuery:function(name) {
            var strHref = window.document.location.href;
            var intPos = strHref.indexOf("?");
            var strRight = strHref.substr(intPos + 1);
            var arrTmp = strRight.split("&");
            for (var i = 0; i < arrTmp.length; i++) {
                var arrTemp = arrTmp[i].split("=");
                if (arrTemp[0].toUpperCase() == name.toUpperCase()) return arrTemp[1];
            }
            if (arguments.length == 1)
                return "";
            if (arguments.length == 2)
                return arguments[1];
        }
    }
});