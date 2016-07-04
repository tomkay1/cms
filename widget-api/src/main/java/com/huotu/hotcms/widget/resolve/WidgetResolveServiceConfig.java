/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Set;

/**
 * 负责装载WidgetService
 *
 * @author CJ
 */
@Configuration
@ComponentScan({"com.huotu.hotcms.widget.resolve.impl", "com.huotu.hotcms.widget.resolve.thymeleaf", "com.huotu.hotcms.widget.service"})
public class WidgetResolveServiceConfig {

    @Autowired
    private Set<IDialect> dialectSet;
    @Autowired
    private ITemplateResolver widgetTemplateResolver;

    /**
     * 这个引擎仅帮助控件模板生成HTML
     *
     * @return Template 引擎
     */
    @Bean
    public SpringTemplateEngine widgetTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setAdditionalDialects(dialectSet);
        engine.setTemplateResolver(widgetTemplateResolver);
        return engine;
    }

}
