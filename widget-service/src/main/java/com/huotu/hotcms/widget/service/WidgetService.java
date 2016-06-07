/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import com.huotu.hotcms.widget.entity.WidgetInfo;

import java.util.List;

/**
 * Created by elvis on 2016/6/7.
 */
public interface WidgetService {

    List<WidgetInfo> getWidgetByOwerId(String owerID);
}
