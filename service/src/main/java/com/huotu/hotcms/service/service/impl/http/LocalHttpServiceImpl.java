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
        appRoot="http://api.open.fancat.cn:8081";
    }
}
