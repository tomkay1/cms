package com.huotu.hotcms.config;

import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.WidgetResolveServiceConfig;
import com.huotu.hotcms.widget.controller.CMSDataSourceController;
import com.huotu.hotcms.widget.controller.WidgetController;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.CMSDataSourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/**
 * Created by lhx on 2016/8/3.
 */
@EnableWebMvc
@Import({WidgetTestConfig.ViewResolver.class, WidgetResolveServiceConfig.class, ServiceConfig.class})
@ComponentScan(value = {"com.huotu.hotcms.bean"})
public class RootConfig extends WebMvcConfigurerAdapter {

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
                .addResourceLocations("widget-test/classpath:/web/public/");
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


}
