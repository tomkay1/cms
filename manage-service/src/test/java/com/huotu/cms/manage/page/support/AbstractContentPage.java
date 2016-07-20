/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page.support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * iframe子页面
 *
 * @author CJ
 */
public abstract class AbstractContentPage extends AbstractManagePage {

    private AbstractFrameParentPage parentPage;

    protected AbstractContentPage(WebDriver webDriver) {
        super(webDriver.findElements(By.id("content")).isEmpty() ? webDriver : webDriver.switchTo().frame("content"));
    }

    public AbstractFrameParentPage getParentPage() {
        return parentPage;
    }

    public void setParentPage(AbstractFrameParentPage parentPage) {
        this.parentPage = parentPage;
    }

    /**
     * @return 就是页面中的body元素
     */
    public abstract WebElement getBody();

    protected void normalValid() {
        try {
            assertThat(getBody().isDisplayed())
                    .isTrue();
        } catch (Throwable ex) {
            printThisPage();
            throw ex;
        }

        try {
            assertNoDanger();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void refresh() {
        if (parentPage != null && AnnotationUtils.findAnnotation(getClass(), BodyId.class) != null) {
            parentPage.toPage(getClass());
            reloadPageInfo();
            return;
        }
        beforeDriver();
        super.refresh();
    }

    /**
     * 在使用driver之前总要确保已经switch到content
     */
    protected void beforeDriver() {
        if (!webDriver.findElements(By.id("content")).isEmpty())
            webDriver.switchTo().frame("content");
    }

    @Override
    public void assertNoDanger() throws InterruptedException {
        webDriver.switchTo().parentFrame();
        super.assertNoDanger();
        beforeDriver();
    }

    @Override
    public void closeDanger() throws InterruptedException {
        webDriver.switchTo().parentFrame();
        super.closeDanger();
        beforeDriver();
    }

    /**
     * 点击table中的一个row中的fa-pencil
     *
     * @param nextPageClass 刷新完成的页面的Class
     * @param rowFilter     只有符合条件的row会被操作;可选项
     * @param <T>           新页面的类型
     * @return 新页面实例
     * @see #consumeElementInTable(Class, Predicate, Consumer)
     */
    public <T extends AbstractManagePage> T clickElementInTable(Class<T> nextPageClass, Predicate<WebElement> rowFilter) {
        return consumeElementInTable(nextPageClass, rowFilter, webElement -> {

            webElement.findElement(By.className("fa-pencil")).click();
            try {
                webDriver.switchTo().alert().accept();
            } catch (Throwable ignored) {
            }
        });
    }

    /**
     * 操作table中的一个row
     *
     * @param nextPageClass 刷新完成的页面的Class
     * @param rowFilter     只有符合条件的row会被操作;可选项
     * @param rowConsumer   处理这个row
     * @param <T>           新页面的类型
     * @return 新页面实例
     */
    public <T extends AbstractManagePage> T consumeElementInTable(Class<T> nextPageClass, Predicate<WebElement> rowFilter
            , Consumer<WebElement> rowConsumer) {
        beforeDriver();
        WebElement table = webDriver.findElement(By.cssSelector("table.table"));

        // fa-pencil
        //
        Stream<WebElement> stream = table.findElements(By.cssSelector("tbody>tr")).stream();
        if (rowFilter != null)
            stream = stream.filter(rowFilter);

        stream.findAny().ifPresent(rowConsumer);
        return initPage(nextPageClass);
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
