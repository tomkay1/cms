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
import com.huotu.hotcms.service.repository.OwnerRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class LoginTest extends ManageTest {

    @Autowired
    private OwnerRepository ownerRepository;

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
    }

    @Test
    public void customer() throws Exception {
        loginAsOwner(ownerRepository.findByCustomerIdNotNull().get(0));
    }

}