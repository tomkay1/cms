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
import com.huotu.hotcms.web.interceptor.SiteResolver;
import com.huotu.hotcms.web.util.CMSDialect;
import com.huotu.hotcms.web.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
        "com.huotu.hotcms.web.thymeleaf.expression"
})
@Import({JpaConfig.class, ServiceConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter {

    private static final String UTF8 = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SiteResolver siteResolver;


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
        registry.viewResolver(redirectViewResolver());
        registry.viewResolver(forwardViewResolver());
        registry.viewResolver(remoteHtmlViewResolver());
    }

    public ViewResolver redirectViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("redirect:*"));
        return resolver;
    }

    public ViewResolver forwardViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("forward:*"));
        return resolver;
    }

    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding(UTF8);
        resolver.setCache(false);
        resolver.setViewNames(ArrayUtil.array("/view/**"));
        return resolver;
    }

    public ViewResolver remoteHtmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(remoteHtmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding(UTF8);
        resolver.setViewNames(ArrayUtil.array("/pc/**"));
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

    private ITemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        List<AbstractProcessorDialect> list= CMSDialect.getDialectList();
        list.forEach(engine::addDialect);
        return engine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private ITemplateResolver remoteHtmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("http://www.huobanj.com");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private ITemplateResolver javascriptTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/assets/js/");
        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        return resolver;
    }

    private ITemplateResolver cssTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/assets/css/");
        resolver.setTemplateMode(TemplateMode.CSS);
        return resolver;
    }
}
