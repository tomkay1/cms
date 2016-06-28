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
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@Transactional
public class ManageControllerTest extends ManageTest {
    @Test
    public void switchCurrentSite() throws Exception {
        Owner owner = randomOwner();
        owner.setCustomerId(Math.abs(random.nextInt()));

        Site site = randomSite(owner);
        mockMvc.perform(get("/manage/switch/{id}", String.valueOf(site.getSiteId())))
                .andExpect(status().isFound());

        loginAsOwner(testOwner);
        mockMvc.perform(get("/manage/switch/{id}", String.valueOf(site.getSiteId())).session(session))
                .andExpect(status().isForbidden());

        loginAsOwner(owner);
        mockMvc.perform(get("/manage/switch/{id}", String.valueOf(site.getSiteId())).session(session))
                .andExpect(status().isOk());

        loginAsManage();

        mockMvc.perform(get("/manage/switch/{id}", String.valueOf(site.getSiteId())).session(session))
                .andExpect(status().isOk());
    }


}