/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web;

import com.huotu.hotcms.widget.servlet.CMSFilter;
import com.huotu.hotcms.widget.servlet.RouteFilter;
import me.jiangcai.lib.test.SpringWebTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {WebTestConfig.class})
@Transactional
@WebAppConfiguration
public abstract class WebTestBase extends SpringWebTest {

    @Override
    public void createMockMVC() {
        super.createMockMVC();
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        builder.addFilter(new CMSFilter(context.getServletContext()));
        builder.addFilter(new RouteFilter(context.getServletContext()));
        if (springSecurityFilter != null) {
            builder = builder.addFilters(springSecurityFilter);
        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
    }
}
