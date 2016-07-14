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

import java.util.List;

/**
 * @author CJ
 */
public abstract class AbstractFrameParentPage extends AbstractManagePage {
    protected AbstractFrameParentPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * 在使用driver之前总要确保已经switch到parent
     */
    protected void beforeDriver() {
        if (webDriver.findElements(By.id("content")).isEmpty())
            webDriver.switchTo().parentFrame();
    }

    public void clickLogout() {
        beforeDriver();
        // 点击最后一个dropdown
        List<WebElement> drops = webDriver.findElements(By.className("dropdown-toggle"));
        WebElement drop = drops.get(drops.size() - 1);
        drop.click();

        webDriver.findElements(By.className("glyphicon-log-out")).forEach(i -> {
            if (i.isDisplayed())
                i.click();
        });
    }

    /**
     * 去指定页面
     *
     * @param pageClazz 页面的类型
     * @param <T>       类型参数
     * @return 新页面实例
     */
    public <T extends AbstractContentPage> T toPage(Class<? extends T> pageClazz) {
        beforeDriver();
        try {
            clickMenuByClass(AnnotationUtils.findAnnotation(pageClazz, BodyId.class).value());
        } catch (NullPointerException ex) {
            throw new IllegalStateException("必须标注BodyId 否则找不到相对的链接:" + pageClazz);
        }
        T page = initPage(pageClazz);
        page.setParentPage(this);
        return page;
    }
}
