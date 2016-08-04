/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.config;

import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.widget.DSConfig;
import me.jiangcai.lib.resource.ResourceSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DSConfig.class, ResourceSpringConfig.class, ServiceConfig.class})
@ComponentScan(value = {"com.huotu.hotcms.widget.config"
        })
public class TestConfig {
}
