/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.OwnerPage;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.service.LoginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@Transactional
public class OwnerControllerTest extends ManageTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 以某一个商户身份运行,并且添加站点
     */
    @Test
    public void asTest() throws Exception {
        loginAsManage();
        Owner owner = randomOwner();

        AdminPage page = initPage(AdminPage.class);
        ManageMainPage mainPage = page.toMainPage(owner);

        // 然后离开这里然后应该回到管理员界面
        mainPage.clickLogout();

        AdminPage adminPage = initPage(AdminPage.class);
    }

    @Test
    public void index() throws Exception {
        loginAsManage();

        AdminPage adminPage = initPage(AdminPage.class);

        OwnerPage ownerPage = adminPage.toOwner();
    }

    @Test
    public void add() throws Exception {
        // 这里走的是实际测试 通过点击选择的菜单 然后进行操作
        loginAsManage();

        AdminPage adminPage = initPage(AdminPage.class);

        OwnerPage ownerPage = adminPage.toOwner();

        // 以商户进行
        int customerId = Math.abs(random.nextInt());
        ownerPage.addOwner(null, null, String.valueOf(customerId));
        assertThat(ownerRepository.findByCustomerId(customerId))
                .isNotNull();

        // 用户密码进行
        String username = randomEmailAddress();
        String password = UUID.randomUUID().toString();
        ownerPage.addOwner(username, password, null);
        assertThat(ownerRepository.findByLoginName(username))
                .isNotNull();

        // 都没用 应该报错
    }

    /**
     *
     */
    @Test
    public void toggleOwnerEnableUrl() throws Exception {
        loginAsManage();

        Owner owner = randomOwner();

        mockMvc.perform(
                put("/manage/supper/owner/{id}/enabled", String.valueOf(owner.getId()))
                        .session(session))
                .andExpect(status().isNoContent());

        assertThat(owner.isEnabled())
                .isFalse();

        mockMvc.perform(
                put("/manage/supper/owner/{id}/enabled", String.valueOf(owner.getId()))
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

    @Test
    public void password() throws Exception {
        loginAsManage();

        Owner owner = randomOwner();

//        int nextCustomerId = Math.abs(random.nextInt());
        String newPassword = UUID.randomUUID().toString();
        mockMvc.perform(
                put("/manage/supper/owner/{id}/password", String.valueOf(owner.getId()))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isNoContent());

//        loginService.loadUserByUsername(owner.getLoginName());
//        assertThat(owner.getCustomerId())
//                .isEqualTo(nextCustomerId);
        assertThat(passwordEncoder.matches(newPassword, owner.getPassword()))
                .isTrue();
    }

}