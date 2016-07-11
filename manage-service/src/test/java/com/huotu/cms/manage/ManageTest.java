/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.test.AuthController;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;

import javax.servlet.http.Cookie;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Qualifier("pageInfoRepository")
    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void aboutTestOwner() {
        testOwner = ownerRepository.findByCustomerIdNotNull().get(0);
    }

    /**
     * 选择一个站点进行管理,会根据当前登录的身份决定是否要进入商户管理模式
     *
     * @param site 要管理的站点
     */
    protected void chooseSite(Site site) throws Exception {
        // driver 版本
        driver.get("http://localhost/manage/main");
        ManageMainPage page;
        try {
            page = initPage(ManageMainPage.class);
        } catch (Exception ex) {
            AdminPage page1 = initPage(AdminPage.class);
            page = page1.toMainPage(site.getOwner());
        }

        page.switchSite(site);

        // mvc 版本
        int code = mockMvc.perform(get("/manage/main").session(session))
                .andReturn().getResponse().getStatus();
        if (code != 200) {
            mockMvc.perform(get("/manage/supper/as/{id}", String.valueOf(site.getOwner().getId())).session(session))
                    .andExpect(status().isFound());
        }

        mockMvc.perform(get("/manage/switch/{id}", String.valueOf(site.getSiteId())).session(session))
                .andExpect(status().isOk());
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
    public ManageMainPage loginAsOwner(Owner owner) throws Exception {
        Cookie cookie = new Cookie(CMSEnums.MallCookieKeyValue.CustomerID.name(), String.valueOf(owner.getCustomerId()));

        accessViaCookie(cookie, owner);

        return initPage(ManageMainPage.class);
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

    public AdminPage loginAsManage() throws Exception {
        accessViaCookie(new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1"), new Manager());


//        driver = createWebDriver()
//        driver.manage().deleteAllCookies();
//        driver.manage().addCookie(new org.openqa.selenium.Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1", ".", "/", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)));
//        driver.manage().addCookie(new org.openqa.selenium.Cookie("JSESSIONID", session.getId(), ".", "/", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)));

        driver.get("http://localhost/manage/main");
//        System.out.println(driver.getPageSource());
        return initPage(AdminPage.class);
    }

    /**
     * @return 新建的随机Owner
     */
    protected Owner randomOwner() {
        Owner owner = new Owner();
        owner.setEnabled(true);
        owner.setCustomerId(Math.abs(random.nextInt()));
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

    /**
     * @return 随机创建的路由JO
     */
    protected Route randomRouteValue() {
        Route route = new Route();
        route.setDescription(UUID.randomUUID().toString());
        route.setRule(UUID.randomUUID().toString());
        route.setTargetUri(UUID.randomUUID().toString());
        return route;
    }

    /**
     *
     * @return 随机创建的数据源
     */
    protected Category randomCategory(){
        Site site = randomSite(randomOwner());
        return randomCategory(site);
    }

    protected Category randomCategory(Site site) {
        Category category=new Category();
        category.setParent(null);
        category.setSite(site);
        category.setName(UUID.randomUUID().toString());
        return categoryRepository.saveAndFlush(category);
    }

    protected PageInfo randomPageInfoValue() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPagePath(UUID.randomUUID().toString());
        pageInfo.setTitle(UUID.randomUUID().toString());
        pageInfo.setPageType(PageType.values()[random.nextInt(PageType.values().length)]);
        return pageInfo;
    }

    /**
     * 创建一个随机的页面信息，包括页面配置
     * @return 页面信息
     * @throws JsonProcessingException jackson相关序列化异常
     */
    public PageInfo randomPageInfo() throws JsonProcessingException {
        PageInfo pageInfo=new PageInfo();
        pageInfo.setSite(randomSite(randomOwner()));
        pageInfo.setCategory(randomCategory());
        XmlMapper xmlMapper=new XmlMapper();
        byte[] pageXml=xmlMapper.writeValueAsString(randomPage()).getBytes();
        pageInfo.setPageSetting(pageXml);
        return pageInfoRepository.saveAndFlush(pageInfo);
    }


    private Page randomPage() {
        Page page = new Page();
        page.setPageIdentity(random.nextLong());
        page.setTitle(UUID.randomUUID().toString());

        List<PageElement> pageElementList = new ArrayList<>();
        //PageElement 要么是Layout，要么是Component；二选一
        int randomNum=random.nextInt(100)+1;
        boolean isLayout=false;
        if(randomNum%2==0)
            isLayout=true;

        int nums = random.nextInt(4)+1;//生成PageElement的随机个数
        //在实际环境中，肯定先存在layout,在layout中，拖入component
        pageElementList.add(randomLayout());
        while (nums-- > 0) {
            if(isLayout)
                pageElementList.add(randomLayout());
            else
                pageElementList.add(randomComponent());
        }
        page.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));

        return page;
    }

    private Component randomComponent() {
        Component component=new Component();
        component.setPreviewHTML(UUID.randomUUID().toString());
        component.setStyleId(UUID.randomUUID().toString());
        component.setWidgetIdentity(UUID.randomUUID().toString());
        ComponentProperties componentProperties =new ComponentProperties();
        componentProperties.put(StringUtil.createRandomStr(random.nextInt(3) + 1),UUID.randomUUID().toString());
        component.setProperties(componentProperties);
        return component;
    }

    private Layout randomLayout() {
        Layout layout = new Layout();
        layout.setValue(UUID.randomUUID().toString());

        List<PageElement> pageElementList = new ArrayList<>();


        //PageElement 要么是Layout，要么是Component；二选一
        int randomNum=random.nextInt(10);
        boolean isLayout=false;
        if(randomNum%2==0)
            isLayout=true;

        int nums = random.nextInt(2);//生成PageElement的随机个数
        while (nums-- > 0) {
            if(isLayout)
                pageElementList.add(randomLayout());
            else
                pageElementList.add(randomComponent());
        }
        layout.setElements(pageElementList.toArray(new PageElement[pageElementList.size()]));
        return layout;
    }
}
