/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.config;

import com.huotu.hotcms.service.config.JpaConfig;
import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.thymeleaf.templateresolver.WidgetTemplateResolver;
import com.huotu.hotcms.service.util.CMSDialect;
import com.huotu.hotcms.web.interceptor.RouteInterceptor;
import com.huotu.hotcms.web.interceptor.SiteResolver;
import com.huotu.hotcms.web.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

/**
 * Created by cwb on 2015/12/30.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.hotcms.web.service",
        "com.huotu.hotcms.web.controller",
        "com.huotu.hotcms.web.interceptor",
        "com.huotu.hotcms.web.util.web",
        "com.huotu.hotcms.service.thymeleaf.expression",
        "com.huotu.hotcms.service.thymeleaf.service"
})
@Import({JpaConfig.class, ServiceConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter {

    private static final String UTF8 = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SiteResolver siteResolver;
    @Autowired
    private Environment environment;

    @Autowired
    private RouteInterceptor routeInterceptor;

    @Autowired
    private ThymeleafViewResolver widgetViewResolver;


    /**
     * 允许访问静态资源
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(siteResolver);
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(htmlViewResolver());
        registry.viewResolver(javascriptViewResolver());
        registry.viewResolver(cssViewResolver());
        registry.viewResolver(widgetViewResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(routeInterceptor);
    }


    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding(UTF8);
        if(environment.acceptsProfiles("development")) {
            resolver.setCache(false);
        }
        resolver.setViewNames(ArrayUtil.array("*.html"));
        return resolver;
    }


    private ViewResolver javascriptViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
        resolver.setContentType("application/javascript");
        resolver.setCharacterEncoding(UTF8);
        resolver.setViewNames(ArrayUtil.array("*.js"));
        return resolver;
    }

    private ViewResolver cssViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(cssTemplateResolver()));
        resolver.setContentType("text/css");
        resolver.setCharacterEncoding(UTF8);
        resolver.setViewNames(ArrayUtil.array("*.css"));
        return resolver;
    }

    @Bean
    public ThymeleafViewResolver widgetViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("*.cshtml"));
        resolver.setCharacterEncoding(UTF8);
        resolver.setTemplateEngine(templateEngine(widgetTemplateResolver()));
        return resolver;
    }


    private ITemplateResolver widgetTemplateResolver() {
        WidgetTemplateResolver resolver = new WidgetTemplateResolver();
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    public ITemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        List<AbstractProcessorDialect> list= CMSDialect.getDialectList();
        list.forEach(engine::addDialect);
        return engine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        if(environment.acceptsProfiles("development")) {
            resolver.setCacheable(false);
        }
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private ITemplateResolver javascriptTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        if(environment.acceptsProfiles("development")) {
            resolver.setCacheable(false);
        }
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        return resolver;
    }

    private ITemplateResolver cssTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        if(environment.acceptsProfiles("development")) {
            resolver.setCacheable(false);
        }
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.CSS);
        return resolver;
    }

}
