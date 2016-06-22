/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.loader;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 这个配置添加了widget的loader支持,增加了thymeleaf的更多方言用于载入控件
 *
 * @author CJ
 */
@Configuration
@ComponentScan("com.huotu.hotcms.widget.loader.thymeleaf")
public class WidgetLoaderConfig {
}
