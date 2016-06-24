/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/19.
 */
@Service
@Profile("container")
public class DynamicHttpServiceImpl extends AbstractHttpServiceImpl {

    private  Log log = LogFactory.getLog(getClass());

    @Autowired
    private void setEnv(Environment env) {
        appKey = env.getProperty("com.huotu.huobanplus.open.api.appid", appKey);
        appSecret = env.getProperty("com.huotu.huobanplus.open.api.appsecrect", appSecret);
        appRoot = env.getProperty("com.huotu.huobanplus.open.api.root", appRoot);
        if(appKey==null) {
            log.error("请设置com.huotu.huobanplus.open.api.appid属性");
            throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.appid属性");
        }
        if(appSecret==null) {
            log.error("请设置com.huotu.huobanplus.open.api.appsecrect属性");
            throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.appsecrect属性");
        }
        if(appRoot==null){
            log.error("请设置com.huotu.huobanplus.open.api.root属性");
           throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.root属性");
        }
    }
}
