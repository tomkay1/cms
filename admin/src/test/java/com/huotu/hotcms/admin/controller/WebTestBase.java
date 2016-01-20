/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.admin.controller;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Administrator on 2016/1/20.
 */
public class WebTestBase {

    protected Cookie cookie;

    /**
     * 自动注入应用程序上下文
     **/
    @Autowired
    protected WebApplicationContext context;
    /**
     * 自动注入servlet上下文
     **/
    @Autowired
    protected ServletContext servletContext;

    /**
     * mock请求
     **/
    @Autowired
    protected MockHttpServletRequest request;

    /**
     * mockMvc等待初始化
     **/
    protected MockMvc mockMvc;

    @Before
    public void creatMockMVC() {
        mockMvc = webAppContextSetup(context).build();
        if(cookie==null) {
            cookie = new Cookie("UserID", "4539");
        }
    }

}
