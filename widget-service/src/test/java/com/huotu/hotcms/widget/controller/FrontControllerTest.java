/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

        mockMvc.perform(get("/preview/{id}/1.css", String.valueOf(noExistingPage)))
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
        category.setContentType(contentService.normalContentTypes()[random.nextInt(contentService.normalContentTypes().length)]);
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
            pageModel.setPageIdentity(pageInfo.getId());
            CMSContext.PutContext(request, response, site);
            pageService.savePage(pageModel, pageInfo.getId());
        } catch (Exception e) {
            throw new IllegalStateException("error", e);
        }

    }

    /**
     * 构造顶部导航组件properties参数
     * @return
     */
    @NotNull
    private ComponentProperties getComponentProperties() {
        ComponentProperties properties = new ComponentProperties();
        properties.put("pagingTColor", "#000000");
        properties.put("pagingHColor", "#000000");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "首页");
        map1.put("pagePath", "");
        map1.put("isParent", "false");
        map1.put("id", 1);

        Map<String, Object> map2 = new HashMap<>();
        List<Map<String, Object>> children = new ArrayList<>();
        Map<String, Object> map21 = new HashMap<>();
        map21.put("name", "公司动态");
        map21.put("pagePath", "");
        map21.put("pid", 2);

        Map<String, Object> map22 = new HashMap<>();
        map22.put("name", "行业动态");
        map22.put("pagePath", "");
        map22.put("pid", 2);
        children.add(map21);
        children.add(map22);

        map2.put("name", "动态资讯");
        map2.put("pagePath", "");
        map2.put("isParent", "true");
        map2.put("children", children);
        map2.put("id", 2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "关于我们");
        map3.put("pagePath", "");
        map3.put("isParent", "false");
        map3.put("id", 3);

        List<Map<String, Object>> navbarPageInfoModels = new ArrayList<>();
        navbarPageInfoModels.add(map1);
        navbarPageInfoModels.add(map2);
        navbarPageInfoModels.add(map3);


        properties.put("pagingTColor", "#000000");
        properties.put("pagingHColor", "#000000");
        properties.put("pageIds",navbarPageInfoModels);
        return properties;
    }

}
