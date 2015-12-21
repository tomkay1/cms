package com.huotu.hotcms.admin.config;

import com.huotu.hotcms.admin.dialect.HotDialect;
import com.huotu.hotcms.admin.interceptor.SiteResolver;
import com.huotu.hotcms.admin.util.ArrayUtil;
import com.huotu.hotcms.config.JpaConfig;
import com.huotu.hotcms.config.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan({
        "com.huotu.hotcms.admin.serivce",
        "com.huotu.hotcms.admin.controller",
        "com.huotu.hotcms.admin.interceptor"
})
@Import({JpaConfig.class, ServiceConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware{

    private static final String UTF8 = "UTF-8";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * for upload
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(UTF8);
        return resolver;
    }

    @Bean
    public SiteResolver siteResolver() {
        SiteResolver siteResolver = new SiteResolver();
        return siteResolver;
    }
    @Autowired
    private SiteResolver siteResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(siteResolver);
    }

    /*@Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(UTF8);
        messageSource.setBasename("messages");
        return messageSource;
    }*/

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        converters.add(converter);
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
        registry.viewResolver(htmlViewResolver());
        registry.viewResolver(javascriptViewResolver());
        registry.viewResolver(cssViewResolver());
    }

    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding(UTF8);
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

    private ITemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        engine.addMessageResolver(messageResolver());
        engine.addDialect(new HotDialect());
        return engine;
    }

    /**
     * 国际化
     * @return
     */
    public IMessageResolver messageResolver() {
        SpringMessageResolver springMessageResolver = new SpringMessageResolver();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding(UTF8);
        springMessageResolver.setMessageSource(messageSource);
        return springMessageResolver;
    }

    private ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCacheable(false);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private ITemplateResolver javascriptTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/assets/js/");
        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        return resolver;
    }

    private ITemplateResolver cssTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/assets/css/");
        resolver.setTemplateMode(TemplateMode.CSS);
        return resolver;
    }
}
