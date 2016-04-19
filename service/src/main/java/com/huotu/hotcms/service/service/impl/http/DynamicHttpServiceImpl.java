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
    private static final Log log = LogFactory.getLog(DynamicHttpServiceImpl.class);

    @Autowired
    private void setEnv(Environment env) {
        this.appKey= env.getProperty("com.huotu.huobanplus.open.api.appid",(String)null);
        this.appSecret = env.getProperty("com.huotu.huobanplus.open.api.appsecrect",(String)null);
        this.appRoot=env.getProperty("com.huotu.huobanplus.open.api.root",(String)null);
        if(this.appKey==null) {
            throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.appid属性");
        }
        if(this.appSecret==null) {
            throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.appsecrect属性");
        }
        if(this.appRoot==null){
           throw new IllegalStateException("请设置com.huotu.huobanplus.open.api.root属性");
        }
    }
}
