/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.config;

import com.huotu.hotcms.service.interceptor.SimulateSiteInterceptor;
import com.huotu.huobanplus.sdk.common.CommonClientSpringConfig;
import com.huotu.huobanplus.sdk.mall.MinMallSDKConfig;
import me.jiangcai.lib.resource.ResourceSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;


@Configuration
@ComponentScan({
        "com.huotu.hotcms.service.converter"
        , "com.huotu.hotcms.service.interceptor"
        , "com.huotu.hotcms.service.service"
        , "com.huotu.hotcms.service.thymeleaf"
        , "com.huotu.hotcms.service.widget.service"
})
@EnableTransactionManagement
@ImportResource({"classpath:spring_dev.xml", "classpath:spring_prod.xml"})
@Import({CommonClientSpringConfig.class, MinMallSDKConfig.class, ResourceSpringConfig.class, JpaConfig.class,})
@EnableWebMvc
public class ServiceConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private SimulateSiteInterceptor simulateSiteInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(simulateSiteInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
