/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class WidgetEditPage extends AbstractContentPage {

    @FindBy(id = "widgetForm")
    private WebElement form;

    public WidgetEditPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        System.out.println(webDriver.getPageSource());
        assertThat(form.isDisplayed())
                .isTrue();
        try {
            assertNoDanger();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    public WidgetPage change(Owner owner, String type, boolean enabled) {
        System.out.println(webDriver.getPageSource());
        if (owner != null)
            inputSelect(form, "ownerId", owner.getUsername());
        inputText(form, "type", type);
        if (owner == null) {
            inputSelect(form, "extra", "无");
        } else {
            inputSelect(form, "extra", owner.getUsername());
        }
        inputChecked(form, "enabled", enabled);
        form.findElement(By.className("btn-primary")).click();
        return initPage(WidgetPage.class);
    }

    private void inputChecked(WebElement form, String name, boolean checked) {
        WebElement input = form.findElement(By.name(name));
        if (checked && inputChecked(input))
            return;
        if (!checked && !inputChecked(input))
            return;
        input.click();
    }

    /**
     * 校验字段
     *
     * @param widgetInfo 期望值
     */
    public void assertObject(WidgetInfo widgetInfo) {
        System.out.println(webDriver.getPageSource());
        assertInputText(form, "groupId", widgetInfo.getGroupId());
        assertInputText(form, "artifactId", widgetInfo.getArtifactId());
        assertInputText(form, "version", widgetInfo.getVersion());
        assertInputText(form, "type", widgetInfo.getType());
        assertInputSelect(form, "extra", widgetInfo.getOwner() != null ? widgetInfo.getOwner().getUsername() : "无");
        assertInputChecked(form, "enabled", widgetInfo.isEnabled());
    }

    private boolean inputChecked(WebElement input) {
        String checked = input.getAttribute("checked");
        if (checked == null)
            return false;
        return checked.equalsIgnoreCase("checked") || checked.equalsIgnoreCase("true");
    }

    /**
     * 校验指定表单中的checked应该是期望的值
     *
     * @param form    表单
     * @param name    input name
     * @param checked 期望值
     */
    private void assertInputChecked(WebElement form, String name, boolean checked) {
        WebElement input = form.findElement(By.name(name));
        assertThat(inputChecked(input))
                .isEqualTo(checked);
    }


    /**
     * 校验指定表单中的select应该是这个label
     *
     * @param form  表单
     * @param name  input name
     * @param label 期望值
     * @see #inputSelect(WebElement, String, String)
     */
    private void assertInputSelect(WebElement form, String name, String label) {
        WebElement input = form.findElement(By.name(name));
        // chosen-single
        if (!input.isDisplayed()) {

            WebElement container = form.findElements(By.className("chosen-container"))
                    .stream()
                    .filter(webElement -> webElement.getAttribute("title") != null && webElement.getAttribute("title")
                            .equals(input.getAttribute("title")))
                    .findAny().orElseThrow(() -> new IllegalStateException("使用了chosen-select,但没看到chosen-container"));

            assertThat(container.findElement(By.className("chosen-single")).getText())
                    .isEqualTo(label);
        } else {
            assertThat(input.getText())
                    .isEqualTo(label);
        }
    }

    /**
     * 校验指定表单里的某个input需是该值
     *
     * @param form  表单
     * @param name  input name
     * @param value 期望值
     * @see #inputText(WebElement, String, String)
     */
    private void assertInputText(WebElement form, String name, String value) {
        WebElement input = form.findElement(By.name(name));
        assertThat(input.getAttribute("value"))
                .isEqualTo(value);
    }
}
