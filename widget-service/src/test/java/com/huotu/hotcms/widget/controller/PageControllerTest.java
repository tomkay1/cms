/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.test.TestBase;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>针对页面服务controller层{@link FrontController}的单元测试</p>
 */
public class PageControllerTest extends TestBase {
    @Autowired(required = false)
    protected MockHttpServletResponse response;

    @Autowired
    private AbstractContentRepository abstractContentRepository;

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
     * 最基本的测试流
     */
    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void page() throws Exception {
        long contentId = 1L;
        String pagePath = "test";
        //构造数据
        pageInitData(contentId, pagePath);

        //case 1存在的path
        int code = mockMvc.perform(get("/_web/{pagePath}", pagePath)
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(HttpStatus.SC_OK).as("存在的path").isEqualTo(code);

        //case 2不存在的path
        code = mockMvc.perform(get("/_web/{pagePath}", "1234")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(HttpStatus.SC_NOT_FOUND).as("不存在的path").isEqualTo(code);

        //case 3存在的contentId和存在的Path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", pagePath, contentId)
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(HttpStatus.SC_OK).as("存在的contentId和存在的Path").isEqualTo(code);

        //case 4不存在的contentId和不存在的path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", "sdfdsf", "1231")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(HttpStatus.SC_NOT_FOUND).as("不存在的contentId和不存在的path").isEqualTo(code);

        //case 5不存在的contentId和存在的path
        code = mockMvc.perform(get("/_web/{pagePath}/{contentId}", pagePath, "123")
                .accept(MediaType.TEXT_HTML)).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(HttpStatus.SC_OK).as("不存在的contentId和存在的path").isEqualTo(code);

    }

    public void pageInitData(Long contentId, String pagePath) {
        Owner owner = ownerRepository.findOne(1L);
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
        site.setSiteType(SiteType.SITE_PC_WEBSITE);
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        String[] domains = new String[]{"localhost"};
        site = siteService.newSite(domains, domains[0], site, Locale.CHINA);

        Category category = new Category();
        category.setParent(null);
        category.setSite(site);
        categoryRepository.saveAndFlush(category);

        Link link = new Link();
        link.setId(contentId);
        link.setCategory(category);
        abstractContentRepository.saveAndFlush(link);
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
            widgetFactoryService.installWidgetInfo(owner, "com.huotu.hotcms.widget.pagingWidget", "pagingWidget", "1.0-SNAPSHOT"
                    , randomType);
            installedWidgets = widgetFactoryService.widgetList(null);
            InstalledWidget installedWidget = installedWidgets != null
                    && installedWidgets.size() > 0 ? installedWidgets.get(0) : null;
            assert installedWidget != null;
            String styleId = installedWidget.getWidget().styles() != null
                    ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);
            component.setWidgetIdentity("com.huotu.hotcms.widget.pagingWidget-pagingWidget:1.0-SNAPSHOT");
            component.setStyleId(styleId);
            Map<String, String> styleClassNames = new HashMap<>();
            styleClassNames.put("page", UUID.randomUUID().toString().replace("-", ""));
            component.setStyleClassNames(styleClassNames);
            ComponentProperties properties = new ComponentProperties();
            properties.put("pageCount", 20);
            properties.put("pagingTColor", "#000000");
            properties.put("pagingHColor", "#000000");
            component.setProperties(properties);
            layoutElement.setElements(new PageElement[]{component});
            Page page = new Page();
            page.setTitle("test");
            page.setPageIdentity(pageInfo.getPageId());
            page.setElements(new PageElement[]{layoutElement});
            CMSContext.PutContext(request, response, site);
            pageService.savePage(page, pageInfo.getPageId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("查找控件列表失败");
        }

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
