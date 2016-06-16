define(function (require, exports, module) {
    var page={},layout={},module={},widget={};
    exports.widgetPage=function(){
        return{
            getInstance:function(){
              return page;
            },
            setModel:function(widgetPage){
                for(var item in widgetPage){
                    page[item]=widgetPage[item];
                }
                return page;
            }
        };
    }
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
});