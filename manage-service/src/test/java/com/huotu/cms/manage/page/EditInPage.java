/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractManagePage;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;

/**
 * @author CJ
 */
public class EditInPage extends AbstractManagePage {

    public EditInPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void beforeDriver() {

    }

    @Override
    public void validatePage() {

    }

    public void chooseRandomOne() {

        WebElement target = webDriver.findElements(By.className("list-group-item"))
                .stream()
                .findAny().orElseThrow(IllegalStateException::new);

//        System.out.println(target);

        new Actions(webDriver)
                .click(target)
//                .moveToElement(target)
//                .doubleClick()
                .perform();
    }

    public boolean ableModify() {
        try {
            checkOut();
            WebElement button = webDriver.findElement(By.cssSelector("button.btn-primary"));
            return button.isEnabled() && button.isDisplayed();
        } finally {
            checkIn();
        }

    }

    private void checkIn() {
        webDriver.switchTo().parentFrame();
    }

    private void checkOut() {
        webDriver.switchTo().frame(webDriver.findElement(By.tagName("iframe")));
    }

    public void submitForm() throws InterruptedException {
        try {
            checkOut();
            WebElement button = webDriver.findElement(By.cssSelector("button.btn-primary"));
            button.click();
//            assertNoDanger();
//            System.out.println(webDriver.getTitle());
//            System.out.println(webDriver.getCurrentUrl());
//            System.out.println(webDriver.getPageSource());
        } finally {
            checkIn();
        }
    }

    public boolean ableInsert() {
        try {
            webDriver.findElement(By.className("add-group"));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }

    }

    public void newRandomData() {
        webDriver.findElement(By.className("add-group")).findElement(By.tagName("a")).click();
        try {
            checkOut();
            // 应该只有一个form
//            System.out.println(webDriver.getPageSource());
            WebElement form = webDriver.findElement(By.tagName("form"));

            WebElement body = form.findElement(By.className("panel-body"));
            if (!body.isDisplayed()) {
                form.findElement(By.cssSelector("a.maximize")).click();
            }

            for (WebElement input : form.findElements(By.tagName("input"))) {
                if (!input.isDisplayed())
                    continue;
                if (input.getAttribute("name") == null)
                    continue;
                if (input.getAttribute("readOnly") != null)
                    continue;
                if (input.getAttribute("readonly") != null)
                    continue;
                String type = input.getAttribute("type");
                if (type.equalsIgnoreCase("hidden"))
                    continue;
                if (type.equalsIgnoreCase("text")) {
                    inputText(form, input.getAttribute("name"), randomString());
                    continue;
                }
                if (type.equalsIgnoreCase("url")) {
                    inputText(form, input.getAttribute("name"), randomHttpURL());
                    continue;
                }
                if (type.equalsIgnoreCase("file"))
                    continue;
                throw new IllegalStateException("无法处理" + type);
            }

            for (WebElement select : form.findElements(By.tagName("select"))) {
                if (!select.isDisplayed())
                    continue;
                if (select.getAttribute("name") == null)
                    continue;
                // 随便找一个选项呗
                select.findElements(By.tagName("option"))
                        .stream()
                        .filter(x -> x.getAttribute("disabled") == null)
                        .findAny()
                        .ifPresent(label -> inputSelect(form, select.getAttribute("name"), label.getText()));
            }
            for (WebElement textarea : form.findElements(By.tagName("textarea"))) {
                if (!textarea.isDisplayed())
                    continue;
                if (textarea.getAttribute("name") == null)
                    continue;

                inputText(form, textarea.getAttribute("name"), randomString());
            }

        } finally {
            checkIn();
        }
    }

    @NotNull
    private String randomString() {
        return RandomStringUtils.randomAlphabetic(new Random().nextInt(5) + 3);
    }

    /**
     * @return 获取随机http url
     */
    protected String randomHttpURL() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder("http://");
        stringBuilder.append(RandomStringUtils.randomAlphabetic(2 + random.nextInt(4)));
        stringBuilder.append(".");
        stringBuilder.append(RandomStringUtils.randomAlphabetic(2 + random.nextInt(4)));
        if (random.nextBoolean()) {
            stringBuilder.append(".");
            stringBuilder.append(RandomStringUtils.randomAlphabetic(2 + random.nextInt(4)));
        }
        return stringBuilder.toString();
    }
}
