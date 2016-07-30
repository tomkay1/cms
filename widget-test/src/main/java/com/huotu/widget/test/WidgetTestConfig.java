/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.WidgetLoaderConfig;
import com.huotu.hotcms.widget.WidgetResolveServiceConfig;
import com.huotu.hotcms.widget.controller.CMSDataSourceController;
import com.huotu.hotcms.widget.controller.WidgetController;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.widget.test.bean.CMSDataSourceServiceImpl;
import com.huotu.widget.test.bean.WidgetHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Set;

/**
 * @author CJ
 */
@EnableWebMvc
@Import({WidgetTestConfig.ViewResolver.class, WidgetResolveServiceConfig.class, ServiceConfig.class})
@ComponentScan("com.huotu.widget.test.bean")
public class WidgetTestConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ThymeleafViewResolver normalViewResolver;
    @Autowired
    private ThymeleafViewResolver javascriptThymeleafViewResolver;

    @Bean
    public CMSDataSourceController cmsDataSourceController() {
        return new CMSDataSourceController();
    }

    @Bean
    public WidgetController widgetController() {
        return new WidgetController();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/manage-resources/**")
                .addResourceLocations("classpath:/web/public/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(normalViewResolver);
        registry.viewResolver(javascriptThymeleafViewResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new Converter<String, WidgetIdentifier>() {
            @Override
            public WidgetIdentifier convert(String source) {
                if (source == null)
                    return null;
                return WidgetIdentifier.valueOf(source);
            }
        });
    }

    @Bean
    public CMSDataSourceService cmsDataSourceService() {
        return new CMSDataSourceServiceImpl();
    }

//    @Bean
//    public SpringTemplateEngine springTemplateEngine(){
//        return new SpringTemplateEngine();
//    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/editor")
//                .setViewName("editor");
//    }

    @DependsOn("widgetHolder")
    @Import(WidgetLoaderConfig.class)
    static class ViewResolver {
        @Autowired
        private WidgetHolder widgetHolder;

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private Set<IDialect> dialectSet;

        @Bean
        public ThymeleafViewResolver normalViewResolver() {
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setApplicationContext(applicationContext);
            templateResolver.setPrefix("classpath:/testPages/");
            templateResolver.setSuffix(".html");

//            widgetTemplateResolver.setOrder(1);

            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.addTemplateResolver(templateResolver);
//            engine.addTemplateResolver(widgetTemplateResolver);
            engine.setAdditionalDialects(dialectSet);

            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setCharacterEncoding("UTF-8");
            resolver.setContentType("text/html;charset=UTF-8");
            resolver.setTemplateEngine(engine);
            resolver.setOrder(3);
            return resolver;
        }

        @Bean
        public ThymeleafViewResolver javascriptThymeleafViewResolver() {

            SpringResourceTemplateResolver rootTemplateResolver =
                    new SpringResourceTemplateResolver();
            rootTemplateResolver.setCheckExistence(true);
            rootTemplateResolver.setPrefix("/_resources/testWidget/js/");
            rootTemplateResolver.setApplicationContext(applicationContext);
            rootTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
            rootTemplateResolver.setCharacterEncoding("UTF-8");

            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.addDialect(new SpringSecurityDialect());
            engine.setTemplateResolver(rootTemplateResolver);

            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setOrder(2);
            resolver.setCharacterEncoding("UTF-8");
            resolver.setTemplateEngine(engine);
            resolver.setApplicationContext(applicationContext);
            resolver.setViewNames(new String[]{"*.js", "*.JS"});
            resolver.setContentType("application/javascript");
            return resolver;
        }


    }

}
