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
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import com.huotu.hotcms.widget.servlet.RouteFilter;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import java.util.List;

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
    private PageInfoRepository pageInfoRepository;

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

    public void access() {
        // 创建一个站点
        Owner owner = randomOwner();
        Site site = randomSite(owner);

        PageInfo anotherPage = new PageInfo();
        anotherPage.setPageType(PageType.Ordinary);
        anotherPage.setSite(site);
        anotherPage.setPagePath("wtf");
        anotherPage.setTitle(randomDomain());
        anotherPage = pageInfoRepository.saveAndFlush(anotherPage);

        PageInfo indexPage = new PageInfo();
        indexPage.setPageType(PageType.Ordinary);
        indexPage.setSite(site);
        indexPage.setPagePath("");
        indexPage.setTitle(randomDomain());

        String linkName = randomDomain();//给链接的名字
        // TODO 这里需要给这个页面增加一个控件 这个控件就是显示一个链接,连接到其他页面
        pageInfoRepository.saveAndFlush(indexPage);

        // 执行预览

        driver.get("http://localhost/?simulateSite=" + site.getSiteId());

        assertThat(driver.getTitle())
                .isEqualTo(indexPage.getTitle());

        // 找到那个链接然后点击它
        List<WebElement> links = driver.findElements(By.tagName("a"));
        links.stream().filter((webElement -> webElement.getText().contains(linkName)))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .click();


        assertThat(driver.getTitle())
                .isEqualTo(anotherPage.getTitle());
    }

}
