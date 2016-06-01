/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;

import java.io.InputStream;
import java.util.List;

/**
 * @author CJ
 */
public interface WidgetService {

    /**
     * @return 已安装控件列表
     */
    List<InstalledWidget> widgetList();

    /**
     * 安装新的控件
     *
     * @param jarFile 工程控件jar包
     * @param type    控件类型
     */
    void installWidget(InputStream jarFile, String type);

    /**
     * 更新已安装的控件
     * <p>
     * 需要检查每一个使用该控件的组件属性是否符合要求。<p/>
     * <p>
     * 如果使用了缓存系统,包括组件缓存和页面缓存,更新以后都需要清理缓存。</p>
     *
     * @param widget  原控件
     * @param jarFile 新的工程控件jar包
     */
    void updateWidget(Widget widget, InputStream jarFile);

    //

    /**
     * 生成预览HTML代码
     *
     * @param widget     控件
     * @param styleId    控件样式id,可选
     * @param ownerId    CMS站点所有者Id,必选
     * @param merchantId 商户id,可选
     * @return HTML Code
     */
    String previewHTML(Widget widget, String styleId, Long ownerId, Long merchantId, ComponentProperties properties);

}
