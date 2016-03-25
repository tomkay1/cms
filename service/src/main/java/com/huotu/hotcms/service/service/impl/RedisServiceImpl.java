/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.RedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * 缓存服务
 * Created by cwb on 2016/3/24.
 */
@Service
public class RedisServiceImpl implements RedisService {

    Jedis jedis = new Jedis("localhost");
    private static final String CMS_WIDGET_KEY = "cms_widget";

    @Override
    public String findByWidgetId(Long widgetId) {
        return jedis.hget(CMS_WIDGET_KEY,String.valueOf(widgetId));
    }

    @Override
    public Boolean isWidgetExists(Long widgetId) {
        return jedis.hexists(CMS_WIDGET_KEY,String.valueOf(widgetId));
    }

    @Override
    public Boolean isConnected() {
        return  jedis.isConnected();
    }
}
