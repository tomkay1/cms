package com.huotu.hotcms.web.service.impl.config;

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
public class DynamicConfigService extends AbstractConfigService {

    private static final Log log = LogFactory.getLog(DynamicConfigService.class);

    @Autowired
    private void setEnv(Environment env) {
        this.mallDomain=env.getProperty("mall.domain",(String) null);
        this.mallResources=env.getProperty("mall.resources",(String)null);
        if(this.mallDomain==null){
            throw new IllegalStateException("请设置mall.domain");
        }
        if(this.mallResources==null){
            throw new IllegalStateException("请设置mall.resources");
        }
    }
}
