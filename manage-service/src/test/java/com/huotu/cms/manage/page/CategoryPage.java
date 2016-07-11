/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.service.entity.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class CategoryPage extends AbstractContentPage {

    @FindBy(id = "fa-bars")
    private WebElement body;
    @FindBy(id = "categoryForm")
    private WebElement form;

    public CategoryPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public WebElement getBody() {
        return body;
    }

    @Override
    public void validatePage() {
        assertThat(body.isDisplayed())
                .isTrue();
        try {
            assertNoDanger();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    public void addCategory(Category category) {
        beforeDriver();
        assertThat(form.isDisplayed())
                .isTrue();
        inputText(form, "name", category.getName());
        inputSelect(form, "contentType", category.getContentType().getValue().toString());
        if (category.getParent() != null) {
            inputSelect(form, "extra", category.getParent().getName());
        }
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
    }
}
