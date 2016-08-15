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
import com.huotu.cms.manage.config.ManageServiceSpringConfig;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.LoginPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.hotcms.service.entity.login.Owner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class LoginTest extends ManageTest {


    /**
     * 以提交form的方式登录
     *
     * @throws Exception
     */
    @Test
    public void normal() throws Exception {
        // 1 是商户可登录
        // 2 是测试管理员可登录

        String password = UUID.randomUUID().toString();
        Owner owner = randomOwner(password);
        String username = owner.getLoginName();

        formLogin(username, password, owner, false);
        driver.get("http://localhost/manage/logout");
        formLogin(ManageServiceSpringConfig.BuildIn_ROOT, ManageServiceSpringConfig.BuildIn_Password, null, false);
        driver.get("http://localhost/manage/logout");
        formLogin(ManageServiceSpringConfig.BuildIn_ROOT, ManageServiceSpringConfig.BuildIn_Password
                + ManageServiceSpringConfig.BuildIn_Password, null, true);
    }

    private void formLogin(String username, String password, Owner owner, boolean badPassword) throws Exception {
        MvcResult result = mockMvc.perform(get("/manage/"))
                .andExpect(status().isFound())
                .andReturn();
        session = (MockHttpSession) result.getRequest().getSession(true);

        String loginPageURL = result.getResponse().getRedirectedUrl();

        Document document = Jsoup.parse(mockMvc.perform(get(loginPageURL).session(session))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        Elements forms = document.getElementsByTag("form");
        assertThat(forms)
                .hasSize(1);

        Element form = forms.get(0);

        String action = form.attr("action");
        assertThat(action)
                .isNotEmpty();

        // username password
        MvcResult result2 = mockMvc.perform(post(action).param("username", username).param("password", password).session(session))
                .andReturn();
//        http://localhost/manage
        while (true) {
            if (result2.getResponse().getStatus() == 200) {
//                System.out.println(result2.getResponse().getContentAsString());
                break;
            }
            if (result2.getResponse().getStatus() == 302) {
                final String redirectedUrl = result2.getResponse().getRedirectedUrl();
                System.out.println(redirectedUrl);
                result2 = mockMvc.perform(get(redirectedUrl).session(session)).andReturn();
            } else
                throw new IllegalStateException("why ?" + result2.getResponse().getStatus());
        }

        driver.manage().deleteAllCookies();
        driver.get("http://localhost/manage/");

        LoginPage page = initPage(LoginPage.class);
        page.login(username, password);

        if (badPassword) {
            page.reloadPageInfo();
            page.assertDanger().contains("用户名或者密码不正确。");
            page.closeDanger();
            return;
        }

        if (owner != null) {
            initPage(ManageMainPage.class);
        } else {
            initPage(AdminPage.class);
        }

    }

    /**
     * 以管理员身份登录 cookie
     *
     * @throws Exception
     */
    @Test
    public void manager() throws Exception {
        loginAsManage();

        AdminPage page = initPage(AdminPage.class);
    }

    /**
     * 以商户身份登录 cookie
     *
     * @throws Exception
     */
    @Test
    public void customer() throws Exception {
        loginAsOwner(testOwner);
    }

}