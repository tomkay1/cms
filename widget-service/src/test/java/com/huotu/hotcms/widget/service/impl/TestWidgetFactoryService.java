/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetInfo;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@WebAppConfiguration
@Transactional
public class TestWidgetFactoryService extends TestBase {

    @Autowired
    HttpServletRequest request;
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
            , InstantiationException {

        String randomType = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "1.0-SNAPSHOT", randomType);
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);

        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "1.0-SNAPSHOT", randomType);

        //检验重新加载
        widgetFactoryService.reloadWidgets();
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);

        String randomType2 = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "2.0-SNAPSHOT", randomType2);
        assertWidgetListContainWidgetName("picCarousel", "2.0-SNAPSHOT", randomType2);


        long contentId = 1L;
        String pagePath = "testPicCarousel";
        pageInitData(contentId, pagePath);

        WidgetInfo widgetInfo = null;
        for (InstalledWidget widget : widgetFactoryService.widgetList(null)) {
            if (randomType2.equals(widget.getType())) {
                assertThat(widget.getWidget().widgetId()).isEqualToIgnoringCase("picCarousel");
                widgetInfo = widgetInfoRepository.findOne(widget.getIdentifier());
            }
        }
        //更新控件
        widgetFactoryService.primary(widgetInfo, false);

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
        assertThat(0).isEqualTo(1);
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
        category.setSerial(UUID.randomUUID().toString());
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
            widgetFactoryService.installWidgetInfo(owner, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                    , "1.0-SNAPSHOT", randomType);
            installedWidgets = widgetFactoryService.widgetList(null);
            InstalledWidget installedWidget = installedWidgets != null
                    && installedWidgets.size() > 0 ? installedWidgets.get(0) : null;
            assert installedWidget != null;
            String styleId = installedWidget.getWidget().styles() != null
                    ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);

            component.setWidgetIdentity(installedWidget.getIdentifier().toString());
            component.setStyleId(styleId);
            component.setId("id12344");
            ComponentProperties properties = new ComponentProperties();
            properties.put("maxImgUrl", new String[]{"1.jpg", "2.jpg"});
            properties.put("minImgUrl", new String[]{"1.jpg", "2.jpg"});
            properties.put("styleTemplate", "html");
            component.setProperties(properties);
            layoutElement.setElements(new PageElement[]{component});
            Page page = new Page();
            page.setTitle("testPicCarousel");
            page.setPageIdentity(pageInfo.getPageId());
            page.setElements(new PageElement[]{layoutElement});
            CMSContext.PutContext(request, response, site);
            pageService.savePage(page, pageInfo.getPageId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("查找控件列表失败");
        }

    }


}
