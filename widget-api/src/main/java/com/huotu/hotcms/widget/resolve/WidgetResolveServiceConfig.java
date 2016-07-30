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
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Set;

/**
 * 负责装载WidgetService
 *
 * @author CJ
 */
@Configuration
@Import(WidgetResolveServiceConfig.WidgetJavascriptResolverLoader.class)
@EnableWebMvc
public class WidgetResolveServiceConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Set<IDialect> dialectSet;
    @Autowired
    private ITemplateResolver widgetTemplateResolver;
    @Autowired
    private ThymeleafViewResolver widgetJavascriptResolver;

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

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
        registry.viewResolver(widgetJavascriptResolver);
    }

    @Import(WidgetJavascriptResolverLoader.SpringTemplateEngineLoader.class)
    static class WidgetJavascriptResolverLoader {

        @Autowired
        private SpringTemplateEngine widgetJavascriptTemplateEngine;
        @Autowired
        private Environment environment;

        @Bean
        public ThymeleafViewResolver widgetJavascriptResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(widgetJavascriptTemplateEngine);
            resolver.setContentType("application/javascript");
            resolver.setCharacterEncoding("UTF-8");
            if (environment.acceptsProfiles("development")) {
                resolver.setCache(false);
            }
            resolver.setViewNames(new String[]{"*.WidgetJS"});
            return resolver;
        }

        @ComponentScan({"com.huotu.hotcms.widget.resolve.impl", "com.huotu.hotcms.widget.resolve.thymeleaf"
                , "com.huotu.hotcms.widget.service"})
        static class SpringTemplateEngineLoader {

            @Autowired
            private ITemplateResolver widgetJavascriptTemplateResolver;

            @Bean
            public SpringTemplateEngine widgetJavascriptTemplateEngine() {
                SpringTemplateEngine engine = new SpringTemplateEngine();
                engine.setTemplateResolver(widgetJavascriptTemplateResolver);
                return engine;
            }
        }

    }
}
