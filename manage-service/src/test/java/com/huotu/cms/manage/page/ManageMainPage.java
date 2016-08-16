/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractFrameParentPage;
import com.huotu.hotcms.service.entity.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商户管理主页面
 *
 * @author CJ
 */
public class ManageMainPage extends AbstractFrameParentPage {
    private static final Log log = LogFactory.getLog(ManageMainPage.class);

    public ManageMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        printThisPage();
        assertThat(webDriver.getTitle())
                .contains("内容管理");
    }


    public RoutePage toRoute() {
        beforeDriver();
        clickMenuByClass("fa-retweet");
        return initPage(RoutePage.class);
    }

    public void switchSite(Site site) {
        beforeDriver();
        // UI去点 可能会有Ajax 异步问题

        WebElement siteList = webDriver.findElement(By.cssSelector("ul.site-list"));
        if (!siteList.isDisplayed()) {
            webDriver.findElement(By.cssSelector("button.site-dropdown")).click();
        }
//        webDriver.get("http://localhost/manage/switch/" + site.getSiteId());
//        webDriver.get("http://localhost/manage/main");
        for (WebElement link : siteList.findElements(By.cssSelector("a.siteSwitcher"))) {
            if (link.getText().contains(site.getName())) {
                link.click();
                reloadPageInfo();
                return;
            }
        }
        throw new IllegalStateException("页面上找不到站点" + site);
    }

    /**
     * 从主页跳转编辑界面
     *
     * @return
     */
    public EditPage toEditPage(long pageId) {
        beforeDriver();
        webDriver.get("http://localhost/manage/page/edit/" + pageId);
        return initPage(EditPage.class);
    }


}
