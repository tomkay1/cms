/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 提供尽量简单的方式,让用户更改widget-properties
 * Created by CJ on 9/16/16.
 */

/**
 * http://docs.huobanplus.com/cms/staging/editor.html
 * 根据新CMS Resource Finder重构,所以这个文件必然是依赖CRF的
 */

CMSWidgets.plugins.properties = {};
CMSWidgets.plugins.properties.util = {};//一些工具方法
CMSWidgets.plugins.properties.globalId = null;
CMSWidgets.plugins.properties.data = {};

/**
 * 工具函数，判断A字符串是否以B字符串结尾
 * @param str       A字符串
 * @param endStr    B字符串
 * @returns {boolean}
 */
CMSWidgets.plugins.properties.util.endWith = function (str, endStr) {
    var d = str.length - endStr.length;
    return (d >= 0 && str.lastIndexOf(endStr) == d)
};


/**
 * 将name作为key在properties关联数组中查找value值
 * @param name      key
 * @returns {*}
 */
CMSWidgets.plugins.properties.util.getValueByProperties = function (name) {
    var properties = widgetProperties(CMSWidgets.plugins.properties.globalId);
    return properties[name];
};

/**
 * 在元素中获取data-name属性的值，如果为undefined则获取name属性的值，
 * 用来当作properties的key
 * @param ele
 * @returns {*}
 */
CMSWidgets.plugins.properties.util.getEleName = function (ele) {
    return $(ele).attr('data-name') || $(ele).attr('name');
};


/**
 * 当编辑器打开的时候调用的函数
 * @param globalId 组件的id
 * @param identity 控件识别符
 * @param editAreaElement 编辑器的jquery结果
 */
CMSWidgets.plugins.properties.open = function (globalId, identity, editAreaElement) {
    CMSWidgets.plugins.properties.globalId = globalId;

    CRF.init(editAreaElement, {
        serial: function () {
            return CMSWidgets.plugins.properties.util.getValueByProperties(CMSWidgets.plugins.properties.util.getEleName(this));
        }, change: function (value) {
            var properties = CMSWidgets.currentEditorProperties();
            properties[CMSWidgets.plugins.properties.util.getEleName(this)] = value.serial;
        }
    });
};




