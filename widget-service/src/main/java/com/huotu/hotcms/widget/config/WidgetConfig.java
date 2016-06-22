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
import org.springframework.context.annotation.ImportResource;

/**
 * Created by wenqi on 2016/6/1.
 */

@Configuration
@ComponentScan("com.huotu.hotcms.widget")
@ImportResource({"classpath:spring_dev.xml", "classpath:spring_prod.xml"})
@Import({WidgetLoaderConfig.class, WidgetResolveServiceConfig.class})
public class WidgetConfig {
}
