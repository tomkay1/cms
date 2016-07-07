/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

/**
 * 控件定位服务
 *
 * @author CJ
 */
public interface WidgetLocateService {

    /**
     * 定位一个控件
     * @param groupId
     * @param widgetId
     * @param version
     * @return 已安装控件实例
     */
    InstalledWidget findWidget(String groupId, String widgetId, String version);

    /**
     * 定位一个控件
     *
     * @param identifier 识别码 { WidgetIdentifier.toString()}
     * @return 已安装控件实例
     */
    InstalledWidget findWidget(String identifier);

}
