/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.RedisService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by cwb on 2016/3/31.
 */
@Service
@Profile("!prod")
public class RedisServiceMockImpl implements RedisService {

    @Override
    public String findByWidgetId(Long widgetId) {
        return null;
    }

    @Override
    public Boolean isWidgetExists(Long widgetId) {
        return false;
    }

    @Override
    public void saveWidget(Long widgetId, String content) {

    }

    @Override
    public String findByWidgetEditId(Long widgetId) {
        return null;
    }

    @Override
    public boolean isWidgetEditExists(Long widgetId) {
        return false;
    }

    @Override
    public void saveWidgetEdit(Long widgetId, String content) {

    }
}
