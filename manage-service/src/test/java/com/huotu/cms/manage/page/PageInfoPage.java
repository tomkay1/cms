/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author CJ
 */
public class PageInfoPage extends AbstractContentPage {

    @FindBy(id = "fa-sitemap")
    private WebElement body;
    @FindBy(id = "pageForm")
    private WebElement form;

    public PageInfoPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public WebElement getBody() {
        return body;
    }

    @Override
    public void validatePage() {
        System.out.println(webDriver.getPageSource());
        normalValid();
    }
}
