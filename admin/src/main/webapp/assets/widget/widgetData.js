/**
 * 编辑页面中存储的变量数据模块,方便管理页面中隐藏的表单域,避免后期出现重复name
 * 方便维护管理
 * **/
var widgetData= {
    saveWidgetSetting: function (widgetSettingJson) {
        var dom = parent.$('#js_widgetSetting_json_value');
        if (typeof dom == "undefined" || dom.length <= 0) {
            parent.$("body").append("<input type='hidden' id='js_widgetSetting_json_value' value='" + widgetSettingJson + "'/>");
        } else {
            parent.$("#js_widgetSetting_json_value").val(widgetSettingJson);
        }
    },
    getWidgetSetting: function () {
        return $('#js_widgetSetting_json_value').val();
    },
    saveTempWidget: function (widgetSettingJson) {
        var dom = $('#js_temp_widget_json_value');
        if (typeof dom == "undefined" || dom.length <= 0) {
            $("body").append("<input type='hidden' id='js_temp_widget_json_value' value='" + widgetSettingJson + "'/>");
        } else {
            $("#js_temp_widget_json_value").val(widgetSettingJson);
        }
    },
    getTempWidgetByParent: function () {
        var widgetValue=parent.$('#js_temp_widget_json_value').val();
        if(typeof widgetValue=="undefined"){
            return null;
        }else{
            return JSON.parse(parent.$('#js_temp_widget_json_value').val());
        }
    },
    saveWidget: function (widgetJson) {
        var dom = parent.$('#js_widget_json_value');
        if (typeof dom == "undefined" || dom.length <= 0) {
            //parent.$("body").append("<input type='hidden' id='js_widget_json_value' value='" + widgetJson + "'/>");
            parent.$("body").append("<script type='application/template' id='js_widget_json_value' >"+widgetJson+"</script>");
        } else {
            parent.$("#js_widget_json_value").html(widgetJson);
        }
    },
    getWidget: function () {
        return $('#js_widget_json_value').val();
    },
    saveLayout: function (layoutJson) {
        var dom = parent.$('#js_layout_json_value');
        if (typeof dom == "undefined" || dom.length <= 0) {
            parent.$("body").append("<input type='hidden' id='js_layout_json_value' value='" + layoutJson + "'/>");
        } else {
            parent.$("#js_layout_json_value").val(layoutJson);
        }
    }
}
