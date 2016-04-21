package com.huotu.hotcms.service.service.impl.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/19.
 */
@Service
@Profile("!container")
public class LocalHttpServiceImpl extends AbstractHttpServiceImpl {
    @Autowired
    private void setEnv() {
        this.appRoot="http://api.open.fancat.cn:8081";
        this.appKey="_demo";
        this.appSecret="1f2f3f4f5f6f7f8f";
    }
}
