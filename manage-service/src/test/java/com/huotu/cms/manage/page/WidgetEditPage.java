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


}
