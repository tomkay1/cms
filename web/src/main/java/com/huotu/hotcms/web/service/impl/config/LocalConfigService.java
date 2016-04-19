package com.huotu.hotcms.web.service.impl.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


/**
 * 本地测试的对接MallApi接口
 * @author xhl
 */
@Service
@Profile("!container")
public class LocalConfigService extends AbstractConfigService {
    private static final Log log = LogFactory.getLog(DynamicConfigService.class);

    @Autowired
    public void setWebApplicationContext(WebApplicationContext context){
        this.mallDomain="huobanj.cn";
        this.mallResources="http://res.51flashmall.com";
    }
}
