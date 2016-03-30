/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.RedisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * 缓存服务
 * Created by cwb on 2016/3/24.
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Log log = LogFactory.getLog(RedisServiceImpl.class);

    private static final String CMS_WIDGET_KEY = "cms_widget";


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private HashOperations<String,Object,String> operations;

    @PostConstruct
    public void init() {
        operations = redisTemplate.opsForHash();
    }

    @Override
    public String findByWidgetId(Long widgetId) {
        return operations.get(CMS_WIDGET_KEY,widgetId);
    }

    @Override
    public Boolean isWidgetExists(Long widgetId) {
        return operations.hasKey(CMS_WIDGET_KEY,widgetId);
    }


    @Override
    public void saveWidget(Long widgetId,String content) {
        operations.put(CMS_WIDGET_KEY,widgetId,content);
    }

}
