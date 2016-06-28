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
import com.huotu.hotcms.widget.resolve.WidgetResolveServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * Created by wenqi on 2016/6/1.
 */

@Configuration
@ComponentScan("com.huotu.hotcms.widget")
@Import({WidgetLoaderConfig.class, WidgetResolveServiceConfig.class,WidgetJpaConfig.class})
public class WidgetConfig {

    @PostConstruct
    public void init(){
        System.out.println("init WidgetConfig...");
    }
}
