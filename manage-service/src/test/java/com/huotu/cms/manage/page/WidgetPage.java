/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.hotcms.widget.entity.WidgetInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class WidgetPage extends AbstractContentPage {

    @FindBy(id = "fa-asterisk")
    private WebElement body;
    @FindBy(id = "widgetForm")
    private WebElement form;

    public WidgetPage(WebDriver webDriver) {
        super(webDriver);
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

    /**
     * 准备提交一个表单,它没有输入专享商户和Jar包
     *
     * @param widgetInfo
     * @return 刷新以后的页面
     */
    public WidgetPage addWidgetWithoutOwnerAndJar(WidgetInfo widgetInfo) {
        inputText(form, "groupId", widgetInfo.getGroupId());
        inputText(form, "widgetId", widgetInfo.getWidgetId());
        inputText(form, "version", widgetInfo.getVersion());
        inputText(form, "type", widgetInfo.getType());
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
        return this;
    }
}
