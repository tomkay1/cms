/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import me.jiangcai.lib.test.page.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * @author CJ
 */
public abstract class AbstractFrameParentPage extends AbstractManagePage {
    AbstractFrameParentPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * 在使用driver之前总要确保已经switch到parent
     */
    void beforeDriver() {
        webDriver.switchTo().parentFrame();
    }

    <T extends AbstractPage> T initPage(Class<T> pageClass) {
        T page = PageFactory.initElements(webDriver, pageClass);
//        page.setResourceService(resourceService);
        page.setTestInstance(getTestInstance());
        page.validatePage();
        return page;
    }


    public void clickLogout() {
        beforeDriver();
        // TODO 运气不错 只有一个 .dropdown-toggle
        webDriver.findElement(By.className("dropdown-toggle")).click();

        webDriver.findElements(By.className("glyphicon-log-out")).forEach(i -> {
            if (i.isDisplayed())
                i.click();
        });
    }
}
