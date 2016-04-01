/**
 +-------------------------------------------------------------------
 * jQuery Jutils - Jutils 公共方法 Jquery插件
 +-------------------------------------------------------------------
 * @version 1.0.0
 * @since 2015.09.25
 * @author xhl <312586670@qq.com>
 +-------------------------------------------------------------------
 */
var JUtils = [];
(function ($) {
    $.extend(JUtils, {
        /*
         *解析模版
         *@param:json json对象
         *@param:template 模版资源
         */
        resolveTemplate: function (json, template) {
            var _html = template;
            for (var key in json) {
                //var _old=/{key}/g;
                var _old = "{" + key + "}";
                _html = _html.replace(new RegExp(_old, "gm"), json[key]);
            }
            return _html;
        },
        serializeJsonByObject:function(obj){
            var Rows={};
            for(var item in obj){
                if($.isArray(item)){
                    Rows[item]=obj[item];
                    //Rows.push(item);
                }
            }
            window.console.log(Rows);
            //obj.Rows=Rows;
            return obj;
        }
    });
    /**
     * @brief: 表单系列化Json对象
     * @return: 返回系列化后的json对象
     */
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
    /**
     * @brief: 表单系列化Json对象
     * @return: 返回系列化后的json对象
     */
    $.fn.serializeJsonList = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var Rows={};
        $(array).each(function () {
            if(this.name.indexOf("|Rows")>0){
                Rows[this.name]=this.value;
            }
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        window.console.log(Rows)
        return serializeObj;
    };
})(jQuery);