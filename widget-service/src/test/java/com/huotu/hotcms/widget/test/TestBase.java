/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.test;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.config.TestConfig;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Empty;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * CMS单元测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public abstract class TestBase extends SpringWebTest {

    //建立一系列已经建立好的控件以及默认属性
    WidgetIdentifier[] preparedWidgets = new WidgetIdentifier[]{
//            new WidgetIdentifier("com.huotu.hotcms.widget.pagingWidget",
//                    "pagingWidget", "1.0-SNAPSHOT")
//            ,
            new WidgetIdentifier("com.huotu.hotcms.widget.picCarousel",
                    "picCarousel", "1.0-SNAPSHOT")
            , new WidgetIdentifier("com.huotu.hotcms.widget.productList",
            "productList", "1.0-SNAPSHOT")
            , new WidgetIdentifier("com.huotu.hotcms.widget.picBanner",
            "picBanner", "1.0-SNAPSHOT")
            , new WidgetIdentifier("com.huotu.hotcms.widget.friendshipLink",
            "friendshipLink", "1.0-SNAPSHOT")
    };
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SiteResolveService siteResolveService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ResourceService resourceService;

    @Override
    public void createMockMVC() {
        MockitoAnnotations.initMocks(this);
        // ignore it, so it works in no-web fine.
        if (context == null)
            return;
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        builder.addFilters(new CMSFilter(context.getServletContext()));
        if (springSecurityFilter != null) {
            builder = builder.addFilters(springSecurityFilter);
        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
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

    protected PageLayout randomPageLayout() throws IOException, FormatException {
        List<Layout> pageElementList = new ArrayList<>();
        int number = random.nextInt(4) + 1;//生成PageElement的随机个数
        while (number-- > 0)
            pageElementList.add(randomLayout());

        return new PageLayout(pageElementList.toArray(new Layout[pageElementList.size()]));
    }

    private Empty randomEmpty() {
        return new Empty();
    }

    protected PageElement randomPageElement() throws IOException, FormatException {
        if (random.nextBoolean())
            return randomEmpty();

        return randomComponent();
    }

    @NotNull
    protected Component randomComponent() throws IOException, FormatException {
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

        List<PageElement> pageElementList = new ArrayList<>();

        while (size-- > 0) {
            //PageElement 要么是Layout，要么是Component；二选一 好像不用管了
            // 大部分情况都是组件了 不然得循环好久呢
            boolean isLayout = random.nextFloat() < 0.1f;
            if (isLayout)
                pageElementList.add(randomLayout());
            else
                pageElementList.add(randomPageElement());
        }

        layout.setParallelElements(pageElementList.toArray(new PageElement[pageElementList.size()]));
        return layout;
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
        String[] domains = randomDomains();
        site = siteService.newSite(domains, domains[0], site, Locale.CHINA);
//        try {
//            site = siteResolveService.getCurrentSite(request);
//        } catch (NoHostFoundException | NoSiteFoundException e) {
//            e.printStackTrace();
//        }
        return site;
    }

    protected String[] randomDomains() {
        int size = 4 + random.nextInt(4);
        String[] domains = (String[]) Array.newInstance(String.class, size);
        for (int i = 0; i < domains.length; i++) {
            domains[i] = randomDomain();
        }
        return domains;
    }

    protected String randomDomain() {
        return RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(5) + 3)
                + "."
                + RandomStringUtils.randomAlphabetic(random.nextInt(2) + 2);
    }

    protected Category randomCategory() {
        Site site = randomSite(randomOwner());
        ContentType contentType = ContentType.values()[random.nextInt(ContentType.values().length)];
        return randomCategory(site, contentType, null);
    }

    protected Category randomCategory(Site site, ContentType contentType) {
        return randomCategory(site, contentType, null);
    }

    protected Category randomCategory(Site site, ContentType contentType, Category parent) {
        Category category = new Category();
        category.setContentType(contentType);
        category.setParent(parent);
        category.setSite(site);
        return categoryRepository.saveAndFlush(category);
    }

    /**
     * 生成老常需要的测试json
     *
     * @return
     */
    protected PageLayout randomNeoTestPageModel() {
        PageLayout pageLayout = new PageLayout();
        Layout layout = new Layout();
        layout.setValue("6,6");
        Component componentA = new Component();
        componentA.setId(UUID.randomUUID().toString());
        componentA.setStyleId(UUID.randomUUID().toString());
        Component componentB = new Component();
        componentB.setId(UUID.randomUUID().toString());
        componentB.setStyleId(UUID.randomUUID().toString());
        Layout layout1 = new Layout();
        layout1.setValue("12");
        Empty empty = new Empty();
        layout1.setParallelElements(new PageElement[]{empty});
        layout.setParallelElements(new PageElement[]{componentA, componentB, layout1});
        pageLayout.setRoot(new Layout[]{layout});
        return pageLayout;
    }

    @NotNull
    protected GalleryItem randomGalleryItem(Gallery gallery) {
        GalleryItem item = new GalleryItem();
        item.setCreateTime(LocalDateTime.now());
        item.setGallery(gallery);
        return galleryItemRepository.save(item);
    }


    protected Gallery randomGallery(Site site) {
        return randomGallery(randomCategory(site, ContentType.Gallery));
    }

    protected Gallery randomGallery(Category category) {
        Gallery gallery = new Gallery();
        gallery.setCategory(category);
        gallery = galleryRepository.saveAndFlush(gallery);
        return gallery;
    }

    @NotNull
    protected Link randomLink(Site site) {
        return randomLink(randomCategory(site, ContentType.Link));
    }

    @NotNull
    protected Link randomLink(Category category) {
        Link link = new Link();
        link.setCategory(category);
        return linkRepository.save(link);
    }
}
