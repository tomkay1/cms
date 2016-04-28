package com.huotu.hotcms.admin.config;

import com.huotu.hotcms.admin.aop.AuthorizeAspect;
import com.huotu.hotcms.admin.dialect.HotDialect;
import com.huotu.hotcms.admin.interceptor.LoginInterceptor;
import com.huotu.hotcms.admin.interceptor.SiteResolver;
import com.huotu.hotcms.admin.util.ArrayUtil;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.config.JpaConfig;
import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.thymeleaf.templateresolver.WidgetTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/16.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.hotcms.admin.service",
        "com.huotu.hotcms.admin.controller",
        "com.huotu.hotcms.admin.interceptor",
        "com.huotu.hotcms.admin.util.web",
        "com.huotu.hotcms.admin.common",
        "com.huotu.hotcms.service.common",
        "com.huotu.hotcms.service.widget",
        "com.huotu.hotcms.service.service",
        "com.huotu.hotcms.service.widget.service"
})
@Import({JpaConfig.class, ServiceConfig.class})
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@EnableAspectJAutoProxy
public class MVCConfig extends WebMvcConfigurerAdapter {

    private static final String UTF8 = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SiteResolver siteResolver;
    @Autowired
    private  LoginInterceptor loginInterceptor;
    @Autowired
    private ThymeleafViewResolver widgetViewResolver;
    @Autowired
    private Set<IDialect> dialectSet;

    @Autowired
    private CookieUser cookieUser;

    @Bean
    public AuthorizeAspect authorizeAspect(){
        return new AuthorizeAspect(cookieUser);
    }

    /**
     * 允许访问静态资源
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * for upload
     * */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(UTF8);
        return resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(siteResolver);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        converters.add(converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
        super.addInterceptors(registry);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(htmlViewResolver());
        registry.viewResolver(javascriptViewResolver());
        registry.viewResolver(cssViewResolver());
        registry.viewResolver(redirectViewResolver());
        registry.viewResolver(forwardViewResolver());
        registry.viewResolver(remoteHtmlViewResolver());
        registry.viewResolver(widgetViewResolver);
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

    @Bean
    public ThymeleafViewResolver widgetViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("*.shtml"));
        resolver.setCharacterEncoding(UTF8);
//        resolver.setTemplateEngine(templateEngine(widgetTemplateResolver()));
//        resolver.setApplicationContext(applicationContext);
        return resolver;
    }

    @Autowired
    public void setWidgetViewResolver(ThymeleafViewResolver widgetViewResolver){
        widgetViewResolver.setTemplateEngine(templateEngine(widgetTemplateResolver()));
    }


    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding(UTF8);
//        resolver.setViewNames(ArrayUtil.array("/view/**"));
        resolver.setViewNames(ArrayUtil.array("*.html"));
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
        engine.addMessageResolver(messageResolver());
        engine.addDialect(new HotDialect());
        dialectSet.forEach(engine::addDialect);
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
        resolver.setCharacterEncoding(UTF8);
        resolver.setApplicationContext(applicationContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private ITemplateResolver widgetTemplateResolver() {
        WidgetTemplateResolver resolver = new WidgetTemplateResolver();
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
