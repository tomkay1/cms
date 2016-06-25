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
import com.huotu.hotcms.service.entity.login.Owner;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@Transactional
public class OwnerControllerTest extends ManageTest {

    @Test
    public void index() throws Exception {
        loginAsManage();

        driver.get("http://localhost/manage/supper/owner");
    }

    /**
     *
     */
    @Test
    public void toggleOwnerEnableUrl() throws Exception {
        loginAsManage();

        Owner owner = randomOwner();

        mockMvc.perform(
                put("/manage/supper/owner/{id}/enable", String.valueOf(owner.getId()))
                        .session(session))
                .andExpect(status().isNoContent());

        assertThat(owner.isEnabled())
                .isFalse();

        mockMvc.perform(
                put("/manage/supper/owner/{id}/enable", String.valueOf(owner.getId()))
                        .session(session))
                .andExpect(status().isNoContent());

        assertThat(owner.isEnabled())
                .isTrue();

    }

    /**
     *
     */
    @Test
    public void customerIdChangeUrl() throws Exception {
        loginAsManage();

        Owner owner = randomOwner();

        int nextCustomerId = Math.abs(random.nextInt());
        mockMvc.perform(
                put("/manage/supper/owner/{id}/customerId", String.valueOf(owner.getId()))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(nextCustomerId)))
                .andExpect(status().isNoContent());

        assertThat(owner.getCustomerId())
                .isEqualTo(nextCustomerId);
    }

}