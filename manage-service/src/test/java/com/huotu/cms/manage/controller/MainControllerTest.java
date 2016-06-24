/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.common.CMSEnums;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {ManageTestConfig.class})
@WebAppConfiguration
public class MainControllerTest extends SpringWebTest {

    @Test
    public void normal() throws Exception {
        String loginPageURL = mockMvc.perform(get("/manage/"))
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        mockMvc.perform(get(loginPageURL))
                .andDo(print());
    }

    @Test
    public void manager() throws Exception {

        MvcResult result = mockMvc.perform(get("/manage/")
                .cookie(new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1")))
                .andExpect(status().isFound())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(true);

        String url = mockMvc.perform(get(result.getResponse().getRedirectedUrl())
                .cookie(new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1"))
                .session(session)
        )
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        mockMvc.perform(get(url).session(session))
                .andDo(print());
    }

    @Test
    public void customer() throws Exception {
        MvcResult result = mockMvc.perform(get("/manage/")
                .cookie(new Cookie(CMSEnums.MallCookieKeyValue.CustomerID.name(), "3447")))
                .andExpect(status().isFound())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(true);

        String url = mockMvc.perform(get(result.getResponse().getRedirectedUrl())
                .cookie(new Cookie(CMSEnums.MallCookieKeyValue.CustomerID.name(), "3447"))
                .session(session)
        )
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        mockMvc.perform(get(url).session(session))
                .andDo(print());
    }

}