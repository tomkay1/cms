/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * iframe子页面
 *
 * @author CJ
 */
abstract class AbstractContentPage extends AbstractManagePage {

    AbstractContentPage(WebDriver webDriver) {
        super(webDriver.findElements(By.id("content")).isEmpty() ? webDriver : webDriver.switchTo().frame("content"));
    }

    /**
     * 在使用driver之前总要确保已经switch到content
     */
    void beforeDriver() {
        if (!webDriver.findElements(By.id("content")).isEmpty())
            webDriver.switchTo().frame("content");
    }

    @Override
    public void assertNoDanger() throws InterruptedException {
        webDriver.switchTo().parentFrame();
        super.assertNoDanger();
        beforeDriver();
    }
}
