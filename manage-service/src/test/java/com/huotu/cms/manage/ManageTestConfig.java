/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.huotu.cms.manage.config.ManageServiceSpringConfig;
import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.thymeleaf.dialect.LoginDialect;
import me.jiangcai.lib.embedweb.ewp.MockMVC;
import me.jiangcai.lib.resource.thymeleaf.ResourceDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * @author CJ
 */
@Configuration
@Import({DSConfig.class, ServiceConfig.class, ManageServiceSpringConfig.class})
@ComponentScan("com.huotu.cms.manage.test")
public class ManageTestConfig extends MockMVC {

    @Autowired
    private ResourceDialect resourceDialect;
    @Autowired
    private LoginDialect loginDialect;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        setTemplateEngineSet(applicationContext.getBeansOfType(TemplateEngine.class).values());
    }

    private void setTemplateEngineSet(Collection<TemplateEngine> templateEngineSet) {
        // 所有都增加安全方言
        templateEngineSet.forEach(engine
                        -> {
                    engine.addDialect(new SpringSecurityDialect());
                    engine.addDialect(resourceDialect);
            engine.addDialect(loginDialect);
                }
        );
    }

    @Autowired
    public void setManageServiceSpringConfig(ManageServiceSpringConfig manageServiceSpringConfig) {
        setEmbedWeb(manageServiceSpringConfig);
    }

}
