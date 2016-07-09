/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.core.io.Resource;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author CJ
 */
public class SitePage extends AbstractContentPage {
    private static final Log log = LogFactory.getLog(SitePage.class);

    @FindBy(id = "fa-puzzle-piece")
    private WebElement body;
    @FindBy(id = "logo-uploader")
    private WebElement uploader;
    @FindBy(id = "addSiteForm")
    private WebElement form;

    public SitePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public WebElement getBody() {
        return body;
    }

    @Override
    public void validatePage() {
        normalValid();
    }

    public void uploadLogo(String name, Resource resource) throws IOException, AWTException, InterruptedException {
        System.out.println(webDriver.getPageSource());
        Path tempFile = Files.createTempFile(name, name);
        Files.copy(resource.getInputStream(), tempFile, REPLACE_EXISTING);
        tempFile.toFile().deleteOnExit();

        System.out.println(uploader.isDisplayed());
        System.out.println(uploader.findElement(By.className("qq-upload-button")).isDisplayed());

        StringSelection stringSelection = new StringSelection(tempFile.toAbsolutePath().toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        uploader.findElement(By.className("qq-upload-button")).click();

//        Thread.sleep(1000);
//        Robot robot = new Robot();
//        click(robot, KeyEvent.VK_ENTER);
//        robot.keyPress(KeyEvent.VK_CONTROL);
//        robot.keyPress(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_CONTROL);
//        robot.keyRelease(KeyEvent.VK_V);
//        click(robot, KeyEvent.VK_ENTER);
//        WebElement uploaderFile = uploader.findElement(By.tagName("input"));
//        uploaderFile.sendKeys(tempFile.toString());
        System.out.println(webDriver.switchTo().activeElement());
        webDriver.switchTo().activeElement().sendKeys(tempFile.toAbsolutePath().toString());
        System.out.println(webDriver.getPageSource());
//        uploader.findElement(By.className("qq-upload-button")).sendKeys(tempFile.toAbsolutePath().toString());
        System.out.println();
    }

    public void addSite(String name, String title, String desc, String[] keywords, String logo, String typeName
            , String copyright, String[] domains, String homeDomain) {
        beforeDriver();
        // 先打开这个添加区域
        WebElement panel = form.findElement(By.className("panel-default"));
        if (panel.getAttribute("class").contains("close-panel")) {
            panel.findElement(By.cssSelector("a.maximize")).click();
        }

//        uploadLogo("thumbnail.png",new ClassPathResource("thumbnail.png"));
        inputHidden(form, "tmpLogoPath", logo);

        inputText(form, "name", name);
        inputText(form, "title", title);
        inputText(form, "description", desc);
        inputTags(form, "keywords", keywords);
        inputSelect(form, "siteTypeId", typeName);
        inputText(form, "copyright", copyright);
        inputTags(form, "domains", domains);
        inputText(form, "homeDomain", homeDomain);

        log.info("to click submit for add site.");
//        System.out.println(webDriver.getPageSource());
        form.findElement(By.className("btn-primary")).click();
        reloadPageInfo();
    }

    private void inputHidden(WebElement form, String name, String value) {
        WebElement input = form.findElement(By.name(name));
        try {
            Field field = HtmlUnitWebElement.class.getDeclaredField("element");
            field.setAccessible(true);
            HtmlInput htmlHiddenInput = (HtmlInput) field.get(input);
            if (value == null)
                htmlHiddenInput.setValueAttribute("");
            else
                htmlHiddenInput.setValueAttribute(value);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }


}
