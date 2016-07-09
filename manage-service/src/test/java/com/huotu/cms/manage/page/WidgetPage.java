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
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        fillAddForm(widgetInfo);
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
        return this;
    }

    private void fillAddForm(WidgetInfo widgetInfo) {
        inputText(form, "groupId", widgetInfo.getGroupId());
        inputText(form, "artifactId", widgetInfo.getArtifactId());
        inputText(form, "version", widgetInfo.getVersion());
        inputText(form, "type", widgetInfo.getType());
    }

    public WidgetPage addWidgetWithOwner(WidgetInfo widgetInfo, Owner owner) {
        fillAddForm(widgetInfo);
        inputSelect(form, "ownerId", owner.getUsername());
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
        return this;
    }

    /**
     * 文件上传还不成熟
     *
     * @param widgetInfo
     * @param owner
     * @param jar
     * @return
     * @throws IOException
     */
    public WidgetPage addWidgetWithOwnerAndJar(WidgetInfo widgetInfo, Owner owner, InputStream jar) throws IOException {
        fillAddForm(widgetInfo);
        inputSelect(form, "ownerId", owner.getUsername());
        File file = File.createTempFile("widgetTest", ".jar");
        file.deleteOnExit();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            StreamUtils.copy(jar, fileOutputStream);
        }
        WebElement fileInput = form.findElement(By.name("jar"));
        fileInput.click();
        webDriver.switchTo().activeElement().sendKeys(file.getAbsolutePath());
//        webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//        LocalFileDetector detector =  new LocalFileDetector();
//        inputText(form,"jar",file.getAbsolutePath());
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
        return this;
    }
}
