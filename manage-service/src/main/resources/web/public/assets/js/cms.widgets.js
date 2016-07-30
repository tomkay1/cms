/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * cms widgets 公用库
 * 技术文档 https://huobanplus.quip.com/iakcAamnwF8U
 * Created by CJ on 7/30/16.
 */

var CMSWidgets = {};

//-------------------------- PUBLIC METHODS

/**
 * 目前依赖于载入的页面支持的global变量CMSDebugMode
 * @return 是否处于调试状态
 */
CMSWidgets.isDebugging = function () {
    return CMSDebugMode;
};

/**
 * 这个方法应该是由编辑器调用
 * 下个控件的识别符,这个方法总是和initWidget配对出现
 * @param identity 控件识别符
 */
CMSWidgets.pushNextWidgetIdentity = function (identity) {
    if (CMSWidgets.nextWidgetIdentity != null) {
        CMSWidgets.initWidget();
    }
    CMSWidgets.nextWidgetIdentity = identity;
};

/**
 * 这个方法推荐由控件自身设置
 * 配置当前控件
 * @param config 可以为null,表示当前控件全部采用默认配置
 */
CMSWidgets.initWidget = function (config) {
    if (CMSWidgets.nextWidgetIdentity == null) {
        console.error('initWidget without pushNextWidgetIdentity');
        return;
    }
    CMSWidgets.initWidgetCore(CMSWidgets.nextWidgetIdentity, config);
    CMSWidgets.nextWidgetIdentity = null;
};

/**
 * 这个方法应该是由编辑器调用
 * 在用户意图打开某一个组件的设置界面时,应该调用这个方法
 * @param globalId 组件的id
 * @param identity 控件识别符
 */
CMSWidgets.openEditor = function (globalId, identity) {
    var config = CMSWidgets.getNoNullConfig(identity, globalId);
    if (CMSWidgets.isDebugging())
        console.error('config on openEditor:', config);
    config.editor.open(globalId);
};
/**
 * 同上
 * 在用户意图关闭某一个组件的设置界面时,应该调用这个方法
 * @param globalId 组件的id
 * @param identity 控件识别符
 */
CMSWidgets.closeEditor = function (globalId, identity) {
    var config = CMSWidgets.getNoNullConfig(identity, globalId);
    config.editor.close(globalId);
};
/**
 * 使用场景：组件使用者点击“保存”时调用。
 * return properties.如果失败则返回null
 *
 * 比如
 * <code>
 *     CMSWidgets.saveComponent("19wdkd8727e73",{
 *                          onSuccess:function(ps){
 *                                console.log('you got me',ps);
 *                          },
 *                          onFailed:function(msg){
 *                                alert(msg);
 *                          }
 *                 })
 * </code>
 * @param globalId 组件的id
 * @param callbacks (可选) 包括 onSuccess(可选) 可以接受 properties作为参数
 *                      onFailed(可选) 可以接受错误描述作为参数
 */
CMSWidgets.saveComponent = function (globalId, callbacks) {
    var config = CMSWidgets.getNoNullConfig(null, globalId);

    if (CMSWidgets.isDebugging())
        console.error('config on saveComponent:', config);

    var voidFunction = function () {

    };
    callbacks = callbacks || {};
    var onSuccess = callbacks.onSuccess || voidFunction;
    var onFailed = callbacks.onFailed || voidFunction;

    return config.editor.saveComponent(onSuccess, onFailed);
};


//-------------------------- PRIVATE
// 默认配置
CMSWidgets.defaultConfig = {
    // TODO 这是应该配置一个所有控件都「应该」做到的事
};
CMSWidgets.nextWidgetIdentity = null;
CMSWidgets.widgetLibraries = {};

CMSWidgets.initWidgetCore = function (identity, config) {
    // 支持null
    CMSWidgets.widgetLibraries[identity] = config;
};

// 不然会返回一个配置
CMSWidgets.getNoNullConfig = function (identity, globalId) {
    if (identity && globalId) {
        $("#" + globalId).attr('widgetIdentity', identity);
    }
    //noinspection JSJQueryEfficiency
    identity = identity || $("#" + globalId).attr('widgetIdentity');
    if (!identity) {
        throw "意图获取一个尚未配置的控件信息";
    }
    var config = CMSWidgets.widgetLibraries[identity];
    if (!config)
        config = CMSWidgets.defaultConfig;
    // 克隆对象及其方法
    var noNullConfig = jQuery.extend(true, {}, config);

    noNullConfig.editor = config.editor || CMSWidgets.defaultConfig.editor;
    var editor = noNullConfig.editor;
    editor.open = editor.open || CMSWidgets.defaultConfig.editor.open;
    editor.saveComponent = editor.saveComponent || CMSWidgets.defaultConfig.editor.saveComponent;

    return noNullConfig;
};

