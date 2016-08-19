/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;

import java.io.IOException;

/**
 * 控件定位服务
 *
 * @author CJ
 */
public interface WidgetLocateService {

    /**
     * 以实例方式直接进行安装
     * <p>这个安装方式是非持久化的</p>
     *
     * @param widget 控件实例
     * @param type   控件类型
     * @param owner  用户
     * @throws IOException 资源处理出现问题
     */
    InstalledWidget installWidget(Owner owner, Widget widget, String type) throws IOException;

    /**
     * 定位一个控件
     *
     * @param groupId
     * @param widgetId
     * @param version
     * @return 已安装控件实例, null 如果没有找到
     */
    InstalledWidget findWidget(String groupId, String widgetId, String version);

    /**
     * 定位一个控件
     *
     * @param identifier 识别码 语法{@link WidgetIdentifier#toString()}
     * @return 已安装控件实例
     */
    InstalledWidget findWidget(String identifier);

}
