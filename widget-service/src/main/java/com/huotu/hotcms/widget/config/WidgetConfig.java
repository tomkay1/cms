/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.config;

import com.huotu.hotcms.widget.loader.WidgetLoaderConfig;
import com.huotu.hotcms.widget.page.PageInfoResolver;
import com.huotu.hotcms.widget.resolve.WidgetResolveServiceConfig;
import com.huotu.hotcms.widget.service.CMSRequestDataValueProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import java.util.List;

@Configuration
@ComponentScan({"com.huotu.hotcms.widget.page"
        , "com.huotu.hotcms.widget.controller"
//        , "com.huotu.hotcms.widget.service"
})
@Import({WidgetLoaderConfig.class, WidgetResolveServiceConfig.class, WidgetJpaConfig.class})
@EnableWebMvc
public class WidgetConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private PageInfoResolver pageInfoResolver;

    @Bean
    public RequestDataValueProcessor
    requestDataValueProcessor() {
        return new CMSRequestDataValueProcessor();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(pageInfoResolver);
    }
}
