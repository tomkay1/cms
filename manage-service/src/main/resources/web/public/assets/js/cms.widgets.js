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

CMSWidgets = CMSWidgets || {};

//-------------------------- PUBLIC METHODS

/**
 * 这个方法应该是由编辑器调用
 * 下个控件的识别符,这个方法总是和initWidget配对出现
 * @param identity 控件识别符
 */
CMSWidgets.pushNextWidgetIdentity = function (identity) {

};

/**
 * 这个方法推荐由控件自身设置
 * 配置当前控件
 * @param config 可以为null,表示当前控件全部采用默认配置
 */
CMSWidgets.initWidget = function (config) {

};

/**
 * 这个方法应该是由编辑器调用
 * 在用户意图打开某一个组件的设置界面时,应该调用这个方法
 * @param globalId 组件的id
 * @param identity 控件识别符
 */
CMSWidgets.openEditor = function (globalId, identity) {

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

};