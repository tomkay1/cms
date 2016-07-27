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
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.cms.manage.test.AuthController;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.TemplateType;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.DownloadRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.TemplateRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Empty;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.util.StreamUtils;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
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

    @Autowired
    private TemplateRepository templateRepository;

    @Qualifier("pageInfoRepository")
    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private DownloadRepository downloadRepository;

    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private GalleryRepository galleryRepository;

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
//        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        site.setKeywords(String.join(",", (CharSequence[]) randomDomains()));
        String[] domains = randomDomains();
        return siteService.newSite(domains, domains[0], site, Locale.CHINA);
    }

    /**
     * 随机一个站点并关联数据源和内容
     *
     * @return
     */
    protected Site randomSiteAndData(Owner owner) {
        Site site = randomSite(owner);
        Category category = randomCategory(site);
        randomArticle(category);
        randomDownload(category);
        Gallery gallery = randomGallery(category);
        randomGalleryItem(gallery);
        return site;
    }

    /**
     * 随机一个模板
     *
     * @return
     */
    protected Template randomTemplate() {
        Template template = new Template();
        template.setUpdateTime(LocalDateTime.now());
        template.setTitle(UUID.randomUUID().toString());
        template.setName(StringUtil.createRandomStr(5));
        TemplateType templateType = new TemplateType();
        templateType.setIndustry(UUID.randomUUID().toString());
        template.setTemplateType(templateType);
        template.setCopyright(UUID.randomUUID().toString());
        template.setDeleted(false);
        template.setEnabled(true);
        template = templateRepository.saveAndFlush(template);
        Category category = randomCategory(template);
        randomArticle(category);
        randomDownload(category);
        Gallery gallery = randomGallery(category);
        randomGalleryItem(gallery);
        return template;
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

    protected void accessViaCookie(Cookie cookie, Login login) throws Exception {
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
     * @return 随机创建的数据源
     */
    protected Category randomCategory() {
        Site site = randomSite(randomOwner());
        return randomCategory(site);
    }

    protected Category randomCategory(Site site) {
        Category category = new Category();
        category.setParent(randomCategory());
        category.setSite(site);
        category.setName(UUID.randomUUID().toString());
        category.setContentType(ContentType.values()[random.nextInt(ContentType.values().length)]);
        return categoryRepository.saveAndFlush(category);
    }

    protected Article randomArticle(Category category) {
        Article article = new Article();
        article.setAuthor(UUID.randomUUID().toString());
        article.setCategory(category);
        return articleRepository.saveAndFlush(article);
    }

    protected Download randomDownload(Category category) {
        Download download = new Download();
        download.setCategory(category);
        download.setTitle(UUID.randomUUID().toString());
        download.setDescription(UUID.randomUUID().toString());
        return downloadRepository.saveAndFlush(download);
    }

    protected GalleryItem randomGalleryItem(Gallery gallery) {
        GalleryItem galleryItem = new GalleryItem();
        galleryItem.setGallery(gallery);
        galleryItem.setCreateTime(LocalDateTime.now());
        return galleryItemRepository.saveAndFlush(galleryItem);
    }

    protected Gallery randomGallery(Category category) {
        Gallery gallery = new Gallery();
        gallery.setCategory(category);
        gallery.setContent(UUID.randomUUID().toString());
        return galleryRepository.saveAndFlush(gallery);
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
     *
     * @return 页面信息
     * @throws JsonProcessingException jackson相关序列化异常
     */
    public PageInfo randomPageInfo() throws IOException, FormatException {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setSite(randomSite(randomOwner()));
        pageInfo.setCategory(randomCategory());
        pageInfo.setPageType(PageType.DataContent);
        pageInfo.setLayout(randomPageLayout());
        return pageInfoRepository.saveAndFlush(pageInfo);
    }

    protected PageLayout randomPageLayout() throws IOException, FormatException {
        List<Layout> pageElementList = new ArrayList<>();
        int number = random.nextInt(4) + 1;//生成PageElement的随机个数
        while (number-- > 0)
            pageElementList.add(randomLayout());

        return new PageLayout(pageElementList.toArray(new Layout[pageElementList.size()]));
    }

    private PageElement randomComponent() throws IOException, FormatException {
        //得预创才可以
        return new Empty();
//        Component component = new Component();
//        component.setPreviewHTML(UUID.randomUUID().toString());
////        component.setStyleId(UUID.randomUUID().toString());
//        String groupId = "com.huotu.hotcms.widget.friendshipLink";
//        String widgetId = "friendshipLink";
//        String version = "1.0-SNAPSHOT";
//        component.setWidgetIdentity(groupId + "-" + widgetId + ":" + version);
//        ComponentProperties properties = new ComponentProperties();
//        Map map = new HashMap<>();
//        List list = new ArrayList<>();
//        map.put("title", UUID.randomUUID().toString());
//        map.put("url", "/wtf");
//        list.add(map);
//        properties.put("linkList", list);
//        properties.put("styleTemplate", "html");
//        InstalledWidget installedWidget = null;
//        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
//        if (installedWidgets == null || installedWidgets.size() == 0) {
//            widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
//                    , "1.0-SNAPSHOT", "picCarousel");
//            installedWidgets = widgetFactoryService.widgetList(null);
//        }
//        WidgetIdentifier widgetIdentifier = null;
//        for (InstalledWidget installedWidget1 : installedWidgets) {
//            widgetIdentifier = installedWidget1.getIdentifier();
//            if (groupId.equals(widgetIdentifier.getGroupId()) && widgetId.equals(widgetIdentifier.getArtifactId()) &&
//                    version.equals(widgetIdentifier.getVersion())) {
//                installedWidget = installedWidget1;
//                break;
//            }
//        }
//        component.setInstalledWidget(installedWidget);
//        return component;
    }

    /**
     * @param size 1 or 2 or 3
     * @return 12 or 1:11 or 1:1:10
     */
    private String randomLayoutValue(int size) {
        int remaining = 12;
        StringBuilder stringBuilder = new StringBuilder();
        Consumer<Integer> newLayout = (value) -> {
            if (stringBuilder.length() != 0)
                stringBuilder.append(',');
            stringBuilder.append(value);
        };
        while (size-- > 0) {
            //此时size表示剩下还有几次
            if (size == 0) {
                newLayout.accept(remaining);
                return stringBuilder.toString();
            }
            //那么至少保证下次还有
            int value = random.nextInt(remaining - size) + 1;
            remaining -= value;
            newLayout.accept(value);
        }
        throw new InternalError("WTF? Bite me");
    }

    private Layout randomLayout() throws IOException, FormatException {
        Layout layout = new Layout();
        //先决定数量
        int size = random.nextInt(3) + 1;
        //再决定分列式 比如 x:y:z
        layout.setValue(randomLayoutValue(size));

        List<PageElement> pageElementList = new ArrayList<>();

        while (size-- > 0) {
            //PageElement 要么是Layout，要么是Component；二选一 好像不用管了
            // 大部分情况都是组件了 不然得循环好久呢
            boolean isLayout = random.nextFloat() < 0.1f;
            if (isLayout)
                pageElementList.add(randomLayout());
            else
                pageElementList.add(randomComponent());
        }

        layout.setParallelElements(pageElementList.toArray(new PageElement[pageElementList.size()]));
        return layout;
    }

    /**
     * 上传文件
     *
     * @param page     操作的页面
     * @param name     隐藏字段的名称
     * @param resource 需要上传的资源
     */
    protected void uploadResource(AbstractCRUDPage<?> page, String name, Resource resource) throws Exception {
        String path = mockMvc.perform(fileUpload("/manage/upload")
                .file("file", StreamUtils.copyToByteArray(resource.getInputStream()))
                .session(session)
        ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        page.inputHidden(page.getForm(), name, path);
    }
}
