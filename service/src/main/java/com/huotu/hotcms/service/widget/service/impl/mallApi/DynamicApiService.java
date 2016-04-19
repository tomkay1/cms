package com.huotu.hotcms.service.widget.service.impl.mallApi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 本地测试的对接MallApi接口
 * @author xhl
 */
@Service
@Profile("container")
public class DynamicApiService extends  AbstractApiService {

    private static final Log log = LogFactory.getLog(DynamicApiService.class);

    @Autowired
    private void setEnv(Environment env) {

        this.serviceRoot= env.getProperty("huotu.mallApi", (String)null);
        if (this.serviceRoot == null) {
            throw new IllegalStateException("请设置huotu.mallApi");
        }
    }
}
