/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.service.entity.Route;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class RoutePage extends AbstractContentPage {

    @FindBy(id = "fa-retweet")
    private WebElement body;
    @FindBy(id = "routeForm")
    private WebElement form;

    public RoutePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
//        System.out.println(webDriver.getPageSource());
        assertThat(body.isDisplayed())
                .isTrue();
        try {
            assertNoDanger();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    public void addRoute(Route route) {
        beforeDriver();
        assertThat(form.isDisplayed())
                .isTrue();
        inputText(form, "rule", route.getRule());
        inputText(form, "targetUri", route.getTargetUri());
        inputText(form, "description", route.getDescription());
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
    }

    /**
     * 点击任意一个.delete
     */
    public void deleteAny() {
        beforeDriver();
        webDriver.findElements(By.className("delete"))
                .stream().findAny().ifPresent(webElement -> {
            webElement.click();
            try {
                webDriver.switchTo().alert().accept();
            } catch (Throwable ignored) {
            }
        });
    }

    /**
     * 打开任意的一个
     *
     * @return 刚打开的页面
     */
    public RoutePage openAny() {
        beforeDriver();
        webDriver.findElements(By.className("fa-pencil"))
                .stream().findAny().ifPresent(webElement -> {
            webElement.click();
            try {
                webDriver.switchTo().alert().accept();
            } catch (Throwable ignored) {
            }
        });
        return initPage(RoutePage.class);
    }

    /**
     * 检查当前打开的值 是否跟传入的值一致
     *
     * @param route 传入Route
     */
    public void checkRouteData(Route route) {
        checkInputText(form, "rule", route.getRule());
        checkInputText(form, "description", route.getDescription());
        checkInputText(form, "targetUri", route.getTargetUri());
    }

    private void checkInputText(WebElement form, String inputName, String value) {
        assertThat(form.findElement(By.name(inputName)).getAttribute("value"))
                .isEqualTo(value);
    }

    /**
     * 修改route
     *
     * @param data 输入的数据
     * @return 修改完成以后的页面
     */
    public RoutePage modifyRoute(Route data) {
        addRoute(data);
        return this;
    }
}
