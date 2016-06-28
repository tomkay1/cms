/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.AdminPage;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class LoginTest extends ManageTest {



    @Test
    public void normal() throws Exception {
        String loginPageURL = mockMvc.perform(get("/manage/"))
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        mockMvc.perform(get(loginPageURL))
                .andExpect(status().isOk());
    }

    @Test
    public void manager() throws Exception {
        loginAsManage();

        AdminPage page = initPage(AdminPage.class);
    }

    @Test
    public void customer() throws Exception {
        loginAsOwner(testOwner);
    }

}