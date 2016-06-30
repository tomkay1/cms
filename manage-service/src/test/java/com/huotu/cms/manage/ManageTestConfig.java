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
import me.jiangcai.lib.embedweb.ewp.MockMVC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Set;

/**
 * @author CJ
 */
@Configuration
@Import({ServiceConfig.class, ManageServiceSpringConfig.class})
@ComponentScan("com.huotu.cms.manage.test")
public class ManageTestConfig extends MockMVC {

    @Autowired
    public void setTemplateEngineSet(Set<SpringTemplateEngine> templateEngineSet) {
        // 所有都增加安全方言
        templateEngineSet.forEach(engine -> engine.addDialect(new SpringSecurityDialect()));
    }

    @Autowired
    public void setManageServiceSpringConfig(ManageServiceSpringConfig manageServiceSpringConfig) {
        setEmbedWeb(manageServiceSpringConfig);
    }

}
