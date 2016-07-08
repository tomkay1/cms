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

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * iframe子页面
 *
 * @author CJ
 */
abstract class AbstractContentPage extends AbstractManagePage {

    AbstractContentPage(WebDriver webDriver) {
        super(webDriver.findElements(By.id("content")).isEmpty() ? webDriver : webDriver.switchTo().frame("content"));
    }

    @Override
    public void refresh() {
        beforeDriver();
        super.refresh();
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

    /**
     * 打开任意的一个
     *
     * @return 刚打开的页面
     */
    public <T extends AbstractManagePage> T openOne(Class<T> clazz, Predicate<WebElement> predicate) {
        beforeDriver();
        WebElement table = webDriver.findElement(By.cssSelector("table.table"));

        // fa-pencil
        //
        Stream<WebElement> stream = table.findElements(By.cssSelector("tbody>tr")).stream();
        if (predicate != null)
            stream = stream.filter(predicate);

        stream.findAny().ifPresent(webElement -> {

            webElement.findElement(By.className("fa-pencil")).click();
            try {
                webDriver.switchTo().alert().accept();
            } catch (Throwable ignored) {
            }
        });
        return initPage(clazz);
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
}
