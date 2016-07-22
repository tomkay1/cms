/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.config.ServiceConfig;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by elvis on 2016/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Import(ServiceConfig.class)
@WebAppConfiguration
@Transactional
@Rollback(true)
public class TestWidgetFactoryService extends TestBase {

    @Autowired
    HttpServletResponse response;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AbstractContentRepository abstractContentRepository;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private PageService pageService;


    @Test
    public void testInstallWidget() throws IOException, FormatException, IllegalAccessException
            , InstantiationException, InterruptedException, PageNotFoundException {


        //*********************************case1 安装控件校验控件是否存在控件列表中******************************
        String randomType = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "1.0-SNAPSHOT", randomType);
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);


        //*********************************case2 检验重新加载后，之前安装的控件有没有被正确加载*********************
        widgetFactoryService.reloadWidgets();
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);

        //*********************************case3 设置主控件包不忽略错误 ，安装新版本控件****************************
        String randomType2 = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "2.0-SNAPSHOT", randomType2);
        assertWidgetListContainWidgetName("picCarousel", "2.0-SNAPSHOT", randomType2);

        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
        InstalledWidget installedWidget = null;
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        assertThat(installedWidget).as("等于null").isNotNull();
        String pagePath = "testPagePath";
        ComponentProperties properties = new ComponentProperties();
        properties.put("maxImgUrl", new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"});
        properties.put("minImgUrl", new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"});
        properties.put("styleTemplate", "html");
        pageInitData(pagePath, installedWidget, properties);
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType2)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        assert installedWidget != null;
        WidgetInfo widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
        widgetFactoryService.primary(widgetInfo, false);
        PageInfo pageInfo = pageInfoRepository.findByPagePath(pagePath);
        Page page = pageService.getPage(pageInfo.getPageId());
        PageElement[] pageElements = page.getElements();
        for (PageElement element : pageElements) {
            validPageElements(element, widgetInfo);
        }
        assertThat(1).as("验证成功，没有出现异常，并找到页面中修改的组件").isEqualTo(1);

        //**********************************case4 设置主控件包不忽略错误 ，安装新版本控件****************************
        pagePath = "testPagePath2";
        properties = new ComponentProperties();
        properties.put("styleTemplate", "html");
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        pageInitData(pagePath, installedWidget, properties);

        try {
            for (InstalledWidget installedWidget1 : installedWidgets) {
                if (installedWidget1.getType().equals(randomType2)) {
                    installedWidget = installedWidget1;
                    break;
                }
            }
            assert installedWidget != null;
            widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
            widgetFactoryService.primary(widgetInfo, false);
            assertThat(0).as("参数验证失败，应当出现异常").isEqualTo(1);
        } catch (IllegalStateException e) {
            assertThat(0).as("参数验证不忽略错误时抛出异常").isEqualTo(0);
        }

        //***************************************case5 安装主控件包，忽略错误***************************************
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        assertThat(installedWidget).as("等于null").isNotNull();
        pagePath = "testPagePath3";
        ComponentProperties properties2 = new ComponentProperties();
        properties.put("maxImgUrl", new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"});
        properties.put("minImgUrl", new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"});
        properties.put("styleTemplate", "html");
        pageInitData(pagePath, installedWidget, properties2);
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType2)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        assert installedWidget != null;
        widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
        widgetFactoryService.primary(widgetInfo, true);


        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getType().equals(randomType)) {
                installedWidget = installedWidget1;
                break;
            }
        }
        widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
        pageInfo = pageInfoRepository.findByPagePath(pagePath);
        page = pageService.getPage(pageInfo.getPageId());
        pageElements = page.getElements();
        for (PageElement element : pageElements) {
            validPageElements(element, widgetInfo);
        }
        assertThat(1).as("页面错误被忽略，组件被忽略更新").isEqualTo(1);

        List<WidgetInfo> list = widgetInfoRepository.findByGroupIdAndArtifactIdAndEnabledTrue(
                "com.huotu.hotcms.widget.picCarousel", "picCarousel");
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getVersion()).as("组件忽略更新，禁用其他低版本的组件").isEqualToIgnoringCase("2.0-SNAPSHOT");

    }

    @Test
    public void testDownloadJar() throws IOException {
        File file = widgetFactoryService.downloadJar("com.huotu.widget.friendshipLink", "friendshipLink", "1.0-SNAPSHOT");
        assertThat(file).as("file不等于空,下载成功").isNotNull();
    }

    public void validPageElements(PageElement pageElement, WidgetInfo widgetInfo) {
        if (pageElement instanceof Layout) {
            PageElement[] layoutElement = ((Layout) pageElement).getElements();
            for (PageElement element : layoutElement) {
                validPageElements(element, widgetInfo);
            }
        } else if (pageElement instanceof Component) {
            Component component = (Component) pageElement;
            assertThat(new WidgetIdentifier(widgetInfo.getGroupId(), widgetInfo.getArtifactId()
                    , widgetInfo.getVersion()).toString()).as("控件被更新成新控件包Identifier")
                    .isEqualToIgnoringCase(component.getWidgetIdentity());
        }
    }

    private void assertWidgetListContainWidgetName(String widgetId, String version, String type) throws IOException, FormatException
            , InstantiationException, IllegalAccessException {
        for (InstalledWidget widget : widgetFactoryService.widgetList(null)) {
            if (type.equals(widget.getType())) {
                assertThat(widget.getWidget().widgetId()).isEqualToIgnoringCase(widgetId);
                assertThat(widget.getWidget().version()).isEqualToIgnoringCase(version);
                return;
            }
        }
        assertThat(0).as("未找到控件").isEqualTo(1);
    }

    public void pageInitData(String pagePath, InstalledWidget installedWidget, ComponentProperties properties) {
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
        category.setSerial(UUID.randomUUID().toString());
        category.setContentType(ContentType.Article);
        categoryRepository.saveAndFlush(category);

        Link link = new Link();
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
        try {
            String styleId = installedWidget.getWidget().styles() != null
                    ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);
            component.setWidgetIdentity(installedWidget.getIdentifier().toString());
            component.setStyleId(styleId);
            component.setId(UUID.randomUUID().toString());

            component.setProperties(properties);
            layoutElement.setElements(new PageElement[]{component});
            Page page = new Page();
            page.setTitle("testPicCarousel");
            page.setPageIdentity(pageInfo.getPageId());
            page.setElements(new Layout[]{layoutElement});
            CMSContext.PutContext(request, response, site);
            pageService.savePage(page, pageInfo.getPageId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("查找控件列表失败");
        }
    }

}
