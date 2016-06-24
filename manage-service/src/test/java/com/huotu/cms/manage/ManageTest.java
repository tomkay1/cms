/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.test.AuthController;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import me.jiangcai.lib.test.SpringWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 提供了自动登录的办法
 *
 * @author CJ
 */
@ContextConfiguration(classes = {ManageTestConfig.class})
@WebAppConfiguration
public abstract class ManageTest extends SpringWebTest {

    protected MockHttpSession session;
    @Autowired
    private AuthController authController;

    /**
     * 以商户身份登录,目前的登录方式 只支持与商户关联的owner
     *
     * @param owner
     */
    public void loginAsOwner(Owner owner) throws Exception {
        Cookie cookie = new Cookie(CMSEnums.MallCookieKeyValue.CustomerID.name(), String.valueOf(owner.getCustomerId()));

        accessViaCookie(cookie, owner);
    }

    private void accessViaCookie(Cookie cookie, Login login) throws Exception {
        MvcResult result = mockMvc.perform(get("/manage/")
                .cookie(cookie))
                .andExpect(status().isFound())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(true);

        String url = mockMvc.perform(get(result.getResponse().getRedirectedUrl())
                .cookie(cookie)
                .session(session)
        )
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        mockMvc.perform(get(url).session(session))
                .andExpect(status().isOk());

//        driver = createWebDriver(cookie);
        driver.manage().deleteAllCookies();
        authController.setLogin(login);
        driver.get("http://localhost/testLoginAs");
    }

    public void loginAsManage() throws Exception {
        accessViaCookie(new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1"), new Manager());


//        driver = createWebDriver()
//        driver.manage().deleteAllCookies();
//        driver.manage().addCookie(new org.openqa.selenium.Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1", ".", "/", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)));
//        driver.manage().addCookie(new org.openqa.selenium.Cookie("JSESSIONID", session.getId(), ".", "/", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)));

        driver.get("http://localhost/manage/main");
        System.out.println(driver.getPageSource());
    }
}
