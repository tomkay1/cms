/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.test.AuthController;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;

import javax.servlet.http.Cookie;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

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
    protected OwnerRepository ownerRepository;
    protected Owner testOwner;
    @Autowired
    private SiteService siteService;
    @Autowired
    private AuthController authController;

    @Before
    public void aboutTestOwner() {
        testOwner = ownerRepository.findByCustomerIdNotNull().get(0);
    }


    /**
     * 建立一个随机的站点
     *
     * @param owner 所属
     * @return 站点
     */
    protected Site randomSite(Owner owner) {
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        String[] domains = randomDomains();
        return siteService.newSite(domains, domains[0], site, Locale.CHINA);
    }

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

        // 一直跳转直到200成功
        while (true) {
            result = mockMvc.perform(get(url).session(session)).andReturn();
            if (result.getResponse().getStatus() == 200)
                break;
            if (result.getResponse().getStatus() == 302)
                url = result.getResponse().getRedirectedUrl();
            else
                throw new IllegalStateException("why ?" + result.getResponse().getStatus());
        }


        driver = MockMvcHtmlUnitDriverBuilder
                .mockMvcSetup(mockMvc)
                .withDelegate(new WebConnectionHtmlUnitDriver(BrowserVersion.CHROME) {
                    @Override
                    protected WebClient modifyWebClientInternal(WebClient webClient) {
//                        webClient.getOptions().setThrowExceptionOnScriptError(false);
                        return super.modifyWebClientInternal(webClient);
                    }
                })
//                .javascriptEnabled(true)
                // DIY by interface.
                .build();
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
//        System.out.println(driver.getPageSource());
    }

    /**
     * @return 新建的随机Owner
     */
    protected Owner randomOwner() {
        Owner owner = new Owner();
        owner.setEnabled(true);
        return ownerRepository.saveAndFlush(owner);
    }

    protected String randomDomain() {
        return RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(2) + 2);
    }

    protected String[] randomDomains() {
        int size = 4 + random.nextInt(4);
        String[] domains = (String[]) Array.newInstance(String.class, size);
        for (int i = 0; i < domains.length; i++) {
            domains[i] = randomDomain();
        }
        return domains;
    }
}
