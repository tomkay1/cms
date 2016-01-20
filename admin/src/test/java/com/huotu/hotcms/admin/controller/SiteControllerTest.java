/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.config.TestAdminConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

/**
 * Created by cwb on 2016/1/20.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAdminConfig.class)
@WebAppConfiguration
@Transactional
public class SiteControllerTest extends WebTestBase {

    @Test
    public void testShowSiteList() throws Exception {
        Cookie cookie = new Cookie("UserID","4539");
        mockMvc.perform(get("/site/siteList").cookie(cookie)
                .param("customerid", "4539")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/view/web/siteList.html"));
    }
}
