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
        appKey= env.getProperty("com.huotu.huobanplus.open.api.appid");
        appSecret = env.getProperty("com.huotu.huobanplus.open.api.appsecrect");
        appRoot=env.getProperty("com.huotu.huobanplus.open.api.root");
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
