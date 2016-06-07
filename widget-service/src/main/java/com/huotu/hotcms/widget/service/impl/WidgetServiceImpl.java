/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.entity.WidgetInfo;
import com.huotu.hotcms.widget.repository.WidgetRepository;
import com.huotu.hotcms.widget.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by elvis on 2016/6/7.
 */
public class WidgetServiceImpl implements WidgetService {

    @Autowired
    private WidgetRepository widgetRepository;


    @Override
    public List<WidgetInfo> getWidgetByOwerId(String owerID) {
        return widgetRepository.findByAuthor(owerID);
    }
}
