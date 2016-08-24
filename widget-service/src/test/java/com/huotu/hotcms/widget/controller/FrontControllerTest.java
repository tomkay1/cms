/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.model.NavbarPageInfoModel;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.test.TestBase;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>针对页面服务controller层{@link FrontController}的单元测试</p>
 */
@Transactional
public class FrontControllerTest extends TestBase {
    @Autowired(required = false)
    protected MockHttpServletResponse response;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Qualifier("pageInfoRepository")
    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Autowired
    private PageService pageService;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private SiteService siteService;


    /**
     * 预览css的测试
     */
    @Test
    public void previewCSS() throws Exception {
        //找一个不存在的Page
        long noExistingPage = Math.abs(random.nextLong());
        while (pageInfoRepository.findOne(noExistingPage) != null)
            noExistingPage = Math.abs(random.nextLong());

        mockMvc.perform(get("/preview/{pageId}/1.css", String.valueOf(noExistingPage)))
                .andDo(print())
                .andExpect(status().isNotFound());

        //建立一个Page
        PageInfo pageInfo = new PageInfo();
        PageLayout layout = randomPageLayout();
    }

    /**
     * 最基本的测试流
     *
     */
    @Test
    public void page() throws Exception {
        long contentId = 1L;
        String pagePath = "test";
        //构造数据
        pageInitData(contentId, pagePath);
        //case 1存在的path
        MockHttpServletResponse response = mockMvc.perform(get("/_web/{pagePath}", pagePath)
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse();
        int code = response.getStatus();
        assertThat(code).as("存在的path").isEqualTo(HttpStatus.SC_OK);
        String html = response.getContentAsString();
        PageInfo pageInfo = pageInfoRepository.findByPagePath(pagePath);
        assertThat(html.contains(pageInfo.getTitle())).as("包含tilte").isEqualTo(true);


        //case 2不存在的path
        code = mockMvc.perform(get("/_web/{pagePath}", "1234")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("不存在的path").isEqualTo(HttpStatus.SC_NOT_FOUND);

        //case 3存在的contentId和存在的Path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", pagePath, contentId)
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("存在的contentId和存在的Path").isEqualTo(HttpStatus.SC_OK);

        //case 4不存在的contentId和不存在的path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", "sdfdsf", "1231")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("不存在的contentId和不存在的path").isEqualTo(HttpStatus.SC_NOT_FOUND);


        //case 5不存在的contentId和存在的path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", pagePath, "123")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).as("不存在的contentId和存在的path").isEqualTo(HttpStatus.SC_OK);


    }


    /**
     * 初始化一个页面，保存指定一个Link（链接模型）内容，并添加一个顶部导航条组件
     *
     * @param contentId 内容id
     * @param pagePath  页面url
     */
    public void pageInitData(Long contentId, String pagePath) {
        Owner owner = ownerRepository.findOne(1L);
        if (owner == null)
            owner = randomOwner();
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
//        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        String[] domains = new String[]{"localhost"};
        site = siteService.newSite(domains, domains[0], site, Locale.CHINA);

        Category category = new Category();
        category.setContentType(ContentType.values()[random.nextInt(ContentType.values().length)]);
        category.setParent(null);
        category.setSite(site);
        categoryRepository.saveAndFlush(category);

        Link link = new Link();
        link.setId(contentId);
        link.setCategory(category);
        contentRepository.saveAndFlush(link);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTitle("test");
        pageInfo.setCategory(category);
        pageInfo.setPagePath(pagePath);
        pageInfo.setPageType(PageType.DataContent);
        pageInfo.setSite(category.getSite());
        pageInfo = pageInfoRepository.saveAndFlush(pageInfo);

        Layout layoutElement = new Layout();
        layoutElement.setValue("12");
        Component component = new Component();
        List<InstalledWidget> installedWidgets;
        try {
            String randomType = UUID.randomUUID().toString();
            // 安装一个demo控件
            widgetFactoryService.installWidgetInfo(owner, "com.huotu.hotcms.widget.topNavigation", "topNavigation"
                    , "1.0-SNAPSHOT", randomType);
            installedWidgets = widgetFactoryService.widgetList(null);
            InstalledWidget installedWidget = installedWidgets != null
                    && installedWidgets.size() > 0 ? installedWidgets.get(0) : null;
            assert installedWidget != null;
            String styleId = installedWidget.getWidget().styles() != null
                    ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);
            component.setWidgetIdentity("com.huotu.hotcms.widget.topNavigation-topNavigation:1.0-SNAPSHOT");
            component.setId(UUID.randomUUID().toString());
            component.setStyleId(styleId);
            ComponentProperties properties = getComponentProperties();
            component.setProperties(properties);
            layoutElement.setParallelElements(new PageElement[]{component});

            PageModel pageModel = new PageModel();
            pageModel.setRoot(new Layout[]{layoutElement});
            pageModel.setPageIdentity(pageInfo.getPageId());
            CMSContext.PutContext(request, response, site);
            pageService.savePage(pageModel, pageInfo.getPageId());
        } catch (Exception e) {
            throw new IllegalStateException("error", e);
        }

    }

    /**
     * 构造顶部导航组件properties参数
     *
     * @return
     */
    @NotNull
    private ComponentProperties getComponentProperties() {
        ComponentProperties properties = new ComponentProperties();
        properties.put("pagingTColor", "#000000");
        properties.put("pagingHColor", "#000000");
        properties.put("logoFileUri", "http://www.baidu.com");
        PageInfo pageInfo1 = new PageInfo();
        pageInfo1.setTitle("首页");
        pageInfo1.setPagePath("");
        pageInfo1.setPageId(1L);

        PageInfo pageInfo2 = new PageInfo();
        pageInfo2.setTitle("新闻");
        pageInfo2.setPagePath("xw");
        pageInfo2.setPageId(2L);

        PageInfo gjxw = new PageInfo();
        gjxw.setTitle("国际新闻");
        gjxw.setPagePath("gjxw");
        gjxw.setPageId(22L);
//        gjxw.setParent(pageInfo2);

        PageInfo gnxw = new PageInfo();
        gnxw.setTitle("国内新闻");
//        gnxw.setParent(pageInfo2);
        gnxw.setPageId(23L);
        gnxw.setPagePath("gnxw");

        PageInfo zjxw = new PageInfo();
        zjxw.setTitle("浙江新闻");
//        zjxw.setParent(gnxw);
        zjxw.setPageId(231L);
        zjxw.setPagePath("zjxw");

        PageInfo pageInfo3 = new PageInfo();
        pageInfo3.setTitle("关于我们");
        pageInfo3.setPagePath("guwm");
        pageInfo3.setPageId(3L);

        List<PageInfo> list = new ArrayList<>();
        list.add(pageInfo1);
        list.add(gjxw);
        list.add(pageInfo2);
        list.add(pageInfo3);
        list.add(gnxw);
        list.add(zjxw);

        List<NavbarPageInfoModel> navbarPageInfoModels = new ArrayList<>();
        for (PageInfo info : list) {
            NavbarPageInfoModel navbarPageInfoModel = new NavbarPageInfoModel();
            navbarPageInfoModel.setName(info.getTitle());
            navbarPageInfoModel.setPagePath(info.getPagePath());
            navbarPageInfoModel.setId(info.getPageId());
//            navbarPageInfoModel.setPid(info.getParent() != null ? info.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        properties.put("pageIds",navbarPageInfoModels);
        return properties;
    }

//    /**
//     * <p>获取页面信息test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#getPage(long)
//     */
//    @Test
//    public void testGetPage() throws Exception {
//        long ownerId = random.nextInt(100);
//        mockMvc.perform(get("/owners/{ownerId}/pages", ownerId)
//                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andReturn();
//    }
//
//    /**
//     * <p>保存界面信息test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#savePage(String, HttpServletRequest)
//     */
//    @Test
//    public void testSavePage() throws Exception {
//        long pageId = random.nextInt(100);
//
//        Page page = randomPage();
//        String json = JSON.toJSONString(page);
//        mockMvc.perform(put("/pages/{pageId}", pageId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//
//        ).andDo(print()).andReturn();
//    }
//
//    /**
//     * <p>新增页面 test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#addPage(long, HttpServletRequest)
//     */
//    @Test
//    public void testAddPage() throws Exception {
//        long ownerId = random.nextInt(100);
//        Page page = randomPage();
//        String json = JSON.toJSONString(page);
//        mockMvc.perform(post("/owners/{ownerId}/pages", ownerId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andDo(print()).andReturn();
//    }
//
//    /**
//     * <p>删除页面 test</p>
//     *
//     * @throws Exception mockMvc异常
//     * @see com.huotu.hotcms.widget.controller.PageController#deletePage(String)
//     */
//    @Test
//    public void testDeletePage() throws Exception {
//        long pageId = random.nextInt(100);
//        mockMvc.perform(delete("/pages/{pageId}", pageId)).andDo(print())
//                .andExpect(status().isAccepted())
//                .andReturn();
//    }
//
//    /**
//     * <p>测试 保存界面部分属性</p>
//     *  @throws Exception mockMvc异常
//     *  @see com.huotu.hotcms.widget.controller.PageController#savePagePartProperties(String, String)
//     */
//    @Test
//    public void testSavePagePartProperties() throws Exception {
//        long pageId = random.nextInt(100);
//        String propertyName= UUID.randomUUID().toString();
//        mockMvc.perform(delete("/pages/{pageId}/{propertyName}", pageId,propertyName)).andDo(print())
//                .andExpect(status().isAccepted())
//                .andReturn();
//    }
}
