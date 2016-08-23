/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractManagePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class LoginPage extends AbstractManagePage {

    @FindBy(tagName = "form")
    private WebElement form;
    @FindBy(name = "username")
    private WebElement username;
    @FindBy(name = "password")
    private WebElement password;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void beforeDriver() {

    }

    @Override
    public void validatePage() {
//        printThisPage();
        assertThat(webDriver.getTitle()).contains("登录");
    }

    public void login(String username, String password) {
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);

        form.findElements(By.tagName("button"))
                .stream()
                .filter(button -> button.getText().contains("登录"))
                .findAny().orElse(null)
                .click();
    }
}
