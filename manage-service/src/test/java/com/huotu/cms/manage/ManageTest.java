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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.cms.manage.page.ManageMainPage;
import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.cms.manage.test.AuthController;
import com.huotu.hotcms.service.ImagesOwner;
import com.huotu.hotcms.service.ResourcesOwner;
import com.huotu.hotcms.service.Serially;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.model.SiteAndSerial;
import com.huotu.hotcms.service.repository.*;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.LoginService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.StyleSheet;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import com.huotu.hotcms.widget.servlet.RouteFilter;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Condition;
import org.assertj.core.util.IterableUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.util.StreamUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * 提供了自动登录的办法
 *
 * @author CJ
 */
@ContextConfiguration(classes = {ManageTestConfig.class})
@WebAppConfiguration
public abstract class ManageTest extends SpringWebTest {

    protected String[][] widgets = new String[][]{
//                new String[]{
//                        "com.huotu.hotcms.widget.pagingWidget",
//                        "pagingWidget"
//                },
//            new String[]{
//                    "com.huotu.hotcms.widget.picCarousel",
//                    "picCarousel"
//            },
//            new String[]{
//                    "com.huotu.hotcms.widget.productList",
//                    "productList"
//            },
            new String[]{
                    "com.huotu.hotcms.widget.picBanner",
                    "picBanner"
            },
            new String[]{
                    "com.huotu.hotcms.widget.copyright",
                    "copyright"
            },
            new String[]{
                    "com.huotu.hotcms.widget.topNavigation",
                    "topNavigation"
            }
    };
    protected MockHttpSession session;
    @Autowired
    protected OwnerRepository ownerRepository;
    protected Owner testOwner;
    @Autowired
    protected PageService pageService;
    @Autowired(required = false)
    protected HttpServletResponse response;
    @Autowired
    protected WidgetInfoRepository widgetInfoRepository;
    @Autowired
    protected WidgetFactoryService widgetFactoryService;
    protected ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected ContentService contentService;
    //建立一系列已经建立好的控件以及默认属性
    WidgetIdentifier[] preparedWidgets = new WidgetIdentifier[]{
//            new WidgetIdentifier("com.huotu.hotcms.widget.pagingWidget",
//                    "pagingWidget", "1.0-SNAPSHOT")
//            ,
            new WidgetIdentifier("com.huotu.hotcms.widget.picCarousel",
                    "picCarousel", "1.0-SNAPSHOT")
//            , new WidgetIdentifier("com.huotu.hotcms.widget.productList",
//            "productList", "1.0-SNAPSHOT")
            , new WidgetIdentifier("com.huotu.hotcms.widget.picBanner",
            "picBanner", "1.0-SNAPSHOT")
            , new WidgetIdentifier("com.huotu.hotcms.widget.friendshipLink",
            "friendshipLink", "1.0-SNAPSHOT")
    };
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
    private ArticleRepository articleRepository;
    @Autowired
    private DownloadRepository downloadRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private LoginService loginService;

    private static <T> Iterable<T> IterableIterator(Iterator<T> iterator) {
        return () -> iterator;
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    protected WidgetInfo randomWidgetInfoValue(Integer seed) {

        String[] widgetInfo;
        if (seed == null) {
            widgetInfo = widgets[random.nextInt(widgets.length)];
        } else {
            widgetInfo = widgets[seed];
        }
        WidgetInfo info = new WidgetInfo();
        // com.huotu.hotcms.widget.pagingWidget  pagingWidget 1.0-SNAPSHOT
        info.setGroupId(widgetInfo[0]);
        info.setArtifactId(widgetInfo[1]);
        info.setVersion("1.0-SNAPSHOT");
        info.setType(randomDomain());

        if (random.nextBoolean()) {
            Owner owner = randomOwner();
            info.setOwner(owner);
        }

        return info;
    }

    protected WidgetInfo randomWidgetInfoValue(String groupId, String artifactId, String version) {

        String[] widgetInfo;

        WidgetInfo info = new WidgetInfo();
        // com.huotu.hotcms.widget.pagingWidget  pagingWidget 1.0-SNAPSHOT
        info.setGroupId(groupId);
        info.setArtifactId(artifactId);
        info.setVersion(version);
//        info.setVersion("1.0-SNAPSHOT");
        info.setType(randomDomain());

        if (random.nextBoolean()) {
            Owner owner = randomOwner();
            info.setOwner(owner);
        }

        return info;
    }

    // 需要应用跟web项目一样的filter
    @Override
    public void createMockMVC() {
        MockitoAnnotations.initMocks(this);
        // ignore it, so it works in no-web fine.
        if (context == null)
            return;
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        builder.addFilters(new CMSFilter(context.getServletContext()), new RouteFilter(context.getServletContext()));
        if (springSecurityFilter != null) {
            builder = builder.addFilters(springSecurityFilter);
        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
    }

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
//        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        site.setKeywords(String.join(",", (CharSequence[]) randomDomains()));
        String[] domains = randomDomains();
        return siteService.newSite(domains, domains[0], site, Locale.CHINA);
    }

    protected Site randomSiteAndNoDomains(Owner owner){
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
//        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        site.setKeywords(String.join(",", (CharSequence[]) randomDomains()));

        return siteService.newSite(null, null, site, Locale.CHINA);
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
     * 给这个站点添加一些数据，包括数据源，正文，页面
     *
     * @param site
     */
    protected void randomSiteData(Site site) throws IOException, FormatException {
        randomSiteData(site, false);
    }

    /**
     * 给这个站点添加一些数据，包括数据源，正文，页面
     *
     * @param site
     * @param all  所有数据都需要
     */
    protected void randomSiteData(Site site, boolean all) throws IOException, FormatException {
        // 包括数据源的父子关系
        while (categoryRepository.findBySite(site).size() < 5) {
            for (ContentType contentType : contentService.normalContentTypes()) {
                if (!contentType.isNormal())
                    continue;
                //create it
                if (random.nextBoolean() || all) {
                    Category category = randomCategoryNoParent(site);
                    category.setContentType(contentType);
                    //parent it?
                    if (random.nextBoolean()) {
                        //找下有没有
                        Category parent = categoryRepository.findBySiteAndContentTypeAndDeletedFalse(site, contentType).stream()
                                .filter((c) -> !c.equals(category))
                                .findAny()
                                .orElse(null);
                        category.setParent(parent);
                        categoryRepository.saveAndFlush(category);
                    }
                }
            }
        }

        // 内容

        while (IterableUtil.sizeOf(contentService.listBySite(site, null)) < 10) {
            for (Category category : categoryRepository.findBySite(site)) {
                if (random.nextBoolean() || all) {
                    AbstractContent content = contentService.newContent(category.getContentType());
                    content.setCategory(category);
                    content.setDescription(UUID.randomUUID().toString());
                    content.setTitle(UUID.randomUUID().toString());

                    if (content instanceof ResourcesOwner) {
                        ResourcesOwner owner = (ResourcesOwner) content;
                        // 如果它同时还是
                        for (int i = 0; i < owner.getResourcePaths().length; i++) {
                            try {
                                owner.updateResource(i, resourceService, randomStream());
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                    }
                    if (content instanceof ImagesOwner) {
                        ImagesOwner owner = (ImagesOwner) content;
                        for (int i = 0; i < owner.getImagePaths().length; i++) {
                            owner.updateImage(i, resourceService, randomImageStream());
                        }
                    }

                    if (content instanceof Article) {
                        Article article = (Article) content;
                        article.setContent(UUID.randomUUID().toString());
                    }
                    if (content instanceof Link) {
                        Link link = (Link) content;
                        link.setLinkUrl(randomHttpURL());
                    }
                    if (content instanceof Notice) {
                        Notice notice = (Notice) content;
                        notice.setContent(UUID.randomUUID().toString());
                    }
                    if (content instanceof Download) {
                        Download download = (Download) content;
                        download.setFileName(randomMobile() + ".png");
                    }

                    contentRepository.save(content);
                }
            }
        }

        // 页面
        while (pageInfoRepository.findBySite(site).size() < 5) {
            PageInfo pageInfo = randomNormalPageInfo(site);
            // 确保它生成了资源 调用savePage
            if (random.nextBoolean()) {
                pageInfo.setPageType(random.nextBoolean() ? PageType.DataContent : PageType.DataIndex);
                pageInfo.setCategory(categoryRepository.findBySite(site).stream()
                        .findAny()
                        .orElse(null));
            }
            pageInfo = pageInfoRepository.saveAndFlush(pageInfo);
            pageService.savePage(pageInfo, null, false);
        }
    }

    /**
     * @return 一个图片流
     */
    private InputStream randomImageStream() throws IOException {
        return new ClassPathResource("thumbnail.png").getInputStream();
    }

    /**
     * @return 任意输入流
     */
    private InputStream randomStream() {
        byte[] data = new byte[10 + random.nextInt(100)];
        random.nextBytes(data);
        return new ByteArrayInputStream(data);
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
        return randomOwner(null);
    }

    protected Owner randomOwner(String rawPassword) {
        Owner owner = new Owner();
        owner.setEnabled(true);
        owner.setCustomerId(Math.abs(random.nextInt()));
        owner.setLoginName(RandomStringUtils.randomAlphabetic(50));
        if (rawPassword != null) {
            loginService.changePassword(owner, rawPassword);
        }
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
        Category category = randomCategoryNoParent(site);
        if (random.nextBoolean())
            category.setParent(randomCategory(site));
        return categoryRepository.saveAndFlush(category);
    }

    /**
     * 纯数据
     *
     * @param site
     * @return
     */
    protected Category randomCategoryData(Site site, ContentType contentType) {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        category.setContentType(contentType);
        category.setSite(site);

        if (random.nextBoolean()) {
            category.setParent(categoryRepository.findBySiteAndContentTypeAndDeletedFalse(site, contentType).stream()
                    .findAny().orElse(null));
        }

        // 看看我有没有parent
        return category;
    }

    protected Category randomCategoryNoParent(Site site) {
        Category category = randomCategoryData(site, contentService.normalContentTypes()[random.nextInt(contentService.normalContentTypes().length)]);
        category.setParent(null);
        categoryService.init(category);
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
        contentService.init(gallery);
//        gallery.setContent(UUID.randomUUID().toString());
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
    protected PageInfo randomPageInfo(Site site) throws IOException, FormatException {
        PageInfo pageInfo = randomNormalPageInfo(site);
        pageInfo.setCategory(randomCategory(site));
        pageInfo.setPageType(PageType.DataContent);
        return pageInfoRepository.saveAndFlush(pageInfo);
    }

    protected PageInfo randomNormalPageInfo(Site site) throws IOException, FormatException {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPagePath(RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3));
        pageInfo.setSite(site);
        pageInfo.setLayout(randomPageLayout());
        pageInfo.setPageType(PageType.Ordinary);
        return pageInfoRepository.saveAndFlush(pageInfo);
    }

    protected PageLayout randomPageLayout() throws IOException, FormatException {
        List<Layout> pageElementList = new ArrayList<>();
        int number = random.nextInt(4) + 1;//生成PageElement的随机个数
        while (number-- > 0)
            pageElementList.add(randomLayout());

        return new PageLayout(pageElementList.toArray(new Layout[pageElementList.size()]), randomStyleSheet());
    }

    private StyleSheet randomStyleSheet() {
        if (random.nextBoolean())
            return null;
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.put("background-color", "#fff/transparent");
        return styleSheet;
    }

    private PageElement randomComponent() throws IOException, FormatException {
        WidgetIdentifier widgetIdentifier = preparedWidgets[random.nextInt(preparedWidgets.length)];
        Component component = makeComponent(widgetIdentifier.getGroupId(), widgetIdentifier.getArtifactId()
                , widgetIdentifier.getVersion());

        ComponentProperties componentProperties = component.getProperties();
        componentProperties.put(StringUtil.createRandomStr(random.nextInt(3) + 1), UUID.randomUUID().toString());
        componentProperties.put("TestArray", new String[]{UUID.randomUUID().toString(), UUID.randomUUID().toString()
                , UUID.randomUUID().toString()});
        componentProperties.put(UUID.randomUUID().toString(), "中文呢?");

        return component;
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
        layout.setStyleSheet(randomStyleSheet());

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
    public void uploadResource(AbstractCRUDPage<?> page, String name, Resource resource) throws Exception {
        String path = mockMvc.perform(fileUpload("/manage/upload")
                .file("file", StreamUtils.copyToByteArray(resource.getInputStream()))
                .session(session)
        ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        page.inputHidden(page.getForm(), name, path);
    }

    protected void updateCMSContext(Site site) {
        CMSContext.PutContext(request, response, site);
    }

    /**
     * 更新一个页面,保证临时数据的生成
     *
     * @param page 页面
     * @throws IOException
     */
    protected void updatePage(PageInfo page) throws IOException {
        try {
            CMSContext.RequestContext();
        } catch (IllegalStateException ignored) {
            CMSContext.PutContext(request, response, page.getSite());
        }
        pageService.savePage(page, null, false);
    }

    /**
     * 让这个页面,只有一个组件
     *
     * @param page      指定页面
     * @param component 指定组件
     * @throws IOException
     */
    protected void updatePageElement(PageInfo page, Component component) throws IOException {
        Layout layout = new Layout();
        layout.setValue("12");
        layout.setParallelElements(new PageElement[]{component});
        page.setLayout(new PageLayout(new Layout[]{layout}, randomStyleSheet()));
        updatePage(page);
    }

    /**
     * 整一个组件出来,默认属性是空
     *
     * @param groupId
     * @param widgetId
     * @param version
     * @return
     * @throws IOException
     * @throws FormatException
     */
    protected Component makeComponent(String groupId, String widgetId, String version) throws IOException, FormatException {
        assertWidget(groupId, widgetId, version);
        Component component = new Component();
        component.setWidgetIdentity(new WidgetIdentifier(groupId, widgetId, version).toString());
        component.setInstalledWidget(widgetFactoryService.installedStatus(widgetInfoRepository.getOne(
                new WidgetIdentifier(groupId, widgetId, version))).get(0));
        component.setProperties(component.getInstalledWidget().getWidget().defaultProperties(resourceService));
        return component;
    }

    /**
     * 保证这个控件必然存在
     *
     * @param groupId
     * @param widgetId
     * @param version
     * @throws IOException
     * @throws FormatException
     */
    protected void assertWidget(String groupId, String widgetId, String version) throws IOException, FormatException {
        WidgetInfo info = widgetInfoRepository.findOne(new WidgetIdentifier(groupId, widgetId, version));
        if (info == null) {
            widgetFactoryService.installWidgetInfo(null, groupId, widgetId, version, "foo");
            info = widgetInfoRepository.getOne(new WidgetIdentifier(groupId, widgetId, version));
        }

        if (widgetFactoryService.installedStatus(info).isEmpty()) {
            widgetFactoryService.installWidgetInfo(info);
        }
    }

    /**
     * 断言这个对象拥有的资源都还在!
     *
     * @param object 资源拥有者
     * @param noNull 所有可拥有的资源都必须存在
     */
    protected void assertResourcesExisting(Object object, boolean noNull) {
        if (object instanceof ResourcesOwner) {
            for (String imagePath : ((ResourcesOwner) object).getResourcePaths()) {
                if (noNull)
                    assertThat(imagePath).isNotNull();

                if (imagePath != null)
                    assertThat(resourceService.getResource(imagePath))
                            .isNotNull()
                            .is(new Condition<>(Resource::exists, ""));
            }
        }
    }

    /**
     * 断言输入json是一个数组,并且结构上跟inputStream类似
     *
     * @param json
     * @param inputStream
     * @throws IOException
     */
    protected void assertSimilarJsonArray(JsonNode json, InputStream inputStream) throws IOException {
        assertThat(json.isArray())
                .isTrue();
        JsonNode mockArray = objectMapper.readTree(inputStream);
        JsonNode mockOne = mockArray.get(0);

        assertSimilarJsonObject(json.get(0), mockOne);
    }

    /**
     * 断言实际json是类似期望json的
     *
     * @param actual
     * @param excepted
     */
    private void assertSimilarJsonObject(JsonNode actual, JsonNode excepted) {
        assertThat(actual.isObject())
                .isTrue();
        assertThat(actual.fieldNames())
                .containsAll(IterableIterator(excepted.fieldNames()));
    }

    @NotNull
    protected SiteAndSerial siteAndSerial(Site site, Serially serial) {
        return siteAndSerial(site, serial.getSerial());
    }

    @NotNull
    protected SiteAndSerial siteAndSerial(Site site, String s) {
        return new SiteAndSerial(site, s);
    }
}
