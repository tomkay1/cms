/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import com.huotu.hotcms.widget.servlet.RouteFilter;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * 预览测试
 * <a href="https://github.com/huotuinc/CMS/issues/8">Issue</a>
 *
 * @author CJ
 */
public class PreviewTest extends ManageTest {

    @Autowired
    protected PageInfoRepository pageInfoRepository;
    @Autowired
    protected WidgetResolveService widgetResolveService;

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

    @Test
    @Transactional
    public void access() throws Exception {
        // 创建一个站点
        Owner owner = randomOwner();
        Site site = randomSite(owner);

        PageInfo anotherPage = new PageInfo();
        anotherPage.setPageType(PageType.Ordinary);
        anotherPage.setSite(site);
        anotherPage.setPagePath("wtf");
        anotherPage.setTitle(randomDomain());
        anotherPage.setLayout(randomPageLayout());
        anotherPage = pageInfoRepository.saveAndFlush(anotherPage);
        updatePage(anotherPage);

        PageInfo indexPage = new PageInfo();
        indexPage.setPageType(PageType.Ordinary);
        indexPage.setSite(site);
        indexPage.setPagePath("");
        indexPage.setTitle(randomDomain());
        indexPage = pageInfoRepository.saveAndFlush(indexPage);

        String linkName = randomDomain();//给链接的名字
        addLinkToPage(indexPage, linkName, anotherPage);
//
//        Component debugIt = (Component) indexPage.getLayout().getElements()[0].getElements()[0];
//        String previeCode = widgetResolveService.previewHTML(debugIt.getInstalledWidget().getWidget(),debugIt.getStyleId()
//                ,CMSContext.RequestContext(),debugIt.getProperties());
//        System.out.println(previeCode);

//        ObjectMapper objectMapper=new ObjectMapper();
//        indexPage.setPageSetting(objectMapper.writeValueAsBytes(randomPage()));

        // 执行预览

//        mockMvc.perform(get("/_web?simulateSite=" + site.getSiteId()))
//                .andDo(print());

        driver.get("http://localhost/?simulateSite=" + site.getSiteId());

//        System.out.println(driver.getPageSource());
        assertThat(driver.getTitle())
                .isEqualTo(indexPage.getTitle());

        // 找到那个链接然后点击它
        List<WebElement> links = driver.findElements(By.tagName("a"));
        links.stream().filter((webElement -> webElement.getText().contains(linkName)))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .click();

//        System.out.println(driver.getPageSource());

        assertThat(driver.getTitle())
                .isEqualTo(anotherPage.getTitle());
    }


    private void addLinkToPage(PageInfo page, String linkTitle, PageInfo toPage) throws IOException, FormatException {
        String groupId = "com.huotu.hotcms.widget.friendshipLink";
        String widgetId = "friendshipLink";
        String version = "1.0-SNAPSHOT";
        Component component = makeComponent(groupId, widgetId, version);

        ComponentProperties properties = component.getProperties();
        Map<String, Object> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        map.put("title", linkTitle);
        map.put("url", "/" + toPage.getPagePath());
        map.put("target", "_self");
        list.add(map);
        properties.put("linkList", list);
        properties.put("styleTemplate", "html");

        updatePageElement(page, component);
    }

}
