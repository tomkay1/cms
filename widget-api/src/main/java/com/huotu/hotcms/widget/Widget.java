/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * 请参考<a href="https://huobanplus.quip.com/KngdAAGxtKSQ">控件技术标准</a>
 *
 * @author CJ
 */
public interface Widget {

    String groupId();

    String widgetId();

    String version();

    String author();

    int dependBuild();

    String name();

    String name(Locale locale);

    String description();

    String description(Locale locale);

    Resource thumbnail();

    Resource editorTemplate();

    WidgetStyle[] styles();

}
