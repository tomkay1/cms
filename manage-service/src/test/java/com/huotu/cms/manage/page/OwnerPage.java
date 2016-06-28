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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商户管理页面
 *
 * @author CJ
 */
public class OwnerPage extends AbstractContentPage {
    @FindBy(id = "addOwnerForm")
    private WebElement addOwnerForm;

    public OwnerPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        assertThat(addOwnerForm.isDisplayed());
    }

    public void addOwner(String username, String password, String customerId) {

        inputText(addOwnerForm, "loginName", username);
        inputText(addOwnerForm, "password", password);
        inputText(addOwnerForm, "customerId", customerId);

        addOwnerForm.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
//        System.out.println(webDriver.getPageSource());
    }
}
