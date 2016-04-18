package com.huotu.hotcms.service.widget.service.impl.mallapi;

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
        String uri = env.getProperty("huotu.mallApi", (String) null);
        this.mallHost=env.getProperty("mall.domain",(String) null);
        this.mallResources=env.getProperty("mall.resources","res.51flashmall.com");
        if (uri == null) {
            throw new IllegalStateException("请设置huotu.mallApi");
        }
        try {
            this.serviceHost =uri;
            this.scheme="http";
        } catch (Exception e) {
            log.error("MallApi环境获取失败", e);
            throw new InternalError("MallApi环境获取失败");
        }
    }
}
