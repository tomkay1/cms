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
import com.huotu.hotcms.service.entity.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;

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

    public void uploadLogo(String name, Resource resource) throws IOException {
        System.out.println(webDriver.getPageSource());
        Path tempFile = Files.createTempFile(name, name);
        Files.copy(resource.getInputStream(), tempFile, REPLACE_EXISTING);
        tempFile.toFile().deleteOnExit();

        System.out.println(uploader.isDisplayed());
        System.out.println(uploader.findElement(By.className("qq-upload-button")).isDisplayed());

//        StringSelection stringSelection = new StringSelection(tempFile.toAbsolutePath().toString());
//        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
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


    public List<WebElement> list() {
        beforeDriver();
        // .panel-body>.row>div
        // //*[@id="fa-puzzle-piece"]/div[2]/div/div[2]/div/div[1]

        return webDriver.findElements(By.cssSelector(".panel-body>.row>div"));
    }

    public Predicate<? super WebElement> findSiteElement(Site site) {
        return webElement -> {
            String id = webElement.getAttribute("data-id");
            return !StringUtils.isEmpty(id) && site.getSiteId().toString().equals(id);
        };
    }

    public Condition<? super WebElement> siteElementCondition(Site site) {
        return new Condition<>(webElement -> {
            boolean result = true;
            WebElement image = webElement.findElement(By.tagName("img"));
            String imageSrc = image.getAttribute("src");

            if (site.getLogoUri() == null) {
                assertThat(imageSrc)
                        .contains(site.getName());
            } else {
                assertThat(imageSrc)
                        .endsWith(site.getLogoUri());
            }

            //应该存在上架 或者下架的button
            List<WebElement> buttons = webElement.findElements(By.tagName("button"));
            assertThat(buttons)
                    .have(new Condition<>(button
                            -> button.getText().equals(site.isEnabled() ? "下架" : "上架"), "需要有存在button"))
                    .doNotHave(new Condition<>(button
                            -> button.getText().equals(site.isEnabled() ? "上架" : "下架"), "需要有存在button"));

            // site-alert 下方显示的名字
            WebElement alert = webElement.findElement(By.className("site-alert"));
            if (site.isAbleToRun()) {
                assertThat(alert.getAttribute("class"))
                        .contains("text-success");
                assertThat(alert.getText())
                        .contains(site.getName());
            } else {
                assertThat(alert.getAttribute("class"))
                        .contains("text-danger");
            }

            //预览按钮
            List<WebElement> previews = webElement.findElements(By.className("site-preview"));
            if (site.isAbleToRun() && site.isEnabled())
                assertThat(previews).isNotEmpty();
            else
                assertThat(previews).isEmpty();

            // 如果site存在logo则路径需是那个
            return true;
        }, "显示信息不正确");
    }
}
