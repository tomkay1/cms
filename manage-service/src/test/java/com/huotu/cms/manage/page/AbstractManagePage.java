/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import me.jiangcai.bracket.test.BracketPage;
import me.jiangcai.lib.test.page.AbstractPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractManagePage extends BracketPage {

    private static final Log log = LogFactory.getLog(AbstractManagePage.class);

    public AbstractManagePage(WebDriver webDriver) {
        super(webDriver);
    }

    public void printThisPage() {
        System.err.println("url:" + webDriver.getCurrentUrl());
        System.err.println(webDriver.getPageSource());
    }

    public void assertNoDanger() throws InterruptedException {
        // 在iframe 页面体系中 错误是由top发起的。所以需要先
        Thread.sleep(100);
        List<String> messages = getGritterMessage("growl-danger");
        assertThat(messages)
                .isEmpty();
    }

    @NotNull
    private List<String> getGritterMessage(String typeClass) {
        List<WebElement> elements = webDriver.findElements(By.className(typeClass));
        List<String> messages = new ArrayList<>();
        if (!elements.isEmpty()) {
            elements.forEach(type -> type.findElements(By.className("gritter-item")).forEach(item -> {
                String message = item.findElement(By.tagName("p")).getText();
                messages.add(message);
            }));
        }
        return messages;
    }

    protected void inputSelect(WebElement formElement, String inputName, String label) {
        WebElement input = formElement.findElement(By.name(inputName));

        if (input.getAttribute("class") != null && input.getAttribute("class").contains("chosen-select")) {
            // 换一个方式
            WebElement container = formElement.findElements(By.className("chosen-container"))
                    .stream()
                    .filter(webElement -> webElement.getAttribute("title") != null && webElement.getAttribute("title")
                            .equals(input.getAttribute("title")))
                    .findAny().orElseThrow(() -> new IllegalStateException("使用了chosen-select,但没看到chosen-container"));

            container.click();
            // TODO 还是不完善的 基本可用 要是数据不是太多的话。
            for (WebElement element : container.findElements(By.cssSelector("li.active-result"))) {
                if (label.equals(element.getText())) {
                    element.click();
                    return;
                }
            }
            return;
        }
        //chosen-container chosen-container-single and same title
        // li.active-result

        input.clear();
        for (WebElement element : input.findElements(By.tagName("option"))) {
//            System.out.println(element.getText());
            if (label.equals(element.getText())) {
                element.click();
                return;
            }
        }
    }

    protected void inputTags(WebElement formElement, String inputName, String[] values) {
        WebElement input = formElement.findElement(By.name(inputName));
        input.clear();
        String id = input.getAttribute("id");
        // 规律是加上 _tag
        WebElement toInput = formElement.findElement(By.id(id + "_tag"));
        for (String value : values) {
            toInput.clear();
            toInput.sendKeys(value);
            toInput.sendKeys(Keys.ENTER);
        }
    }

    /**
     * 在指定表单中输入值type=text
     *
     * @param formElement 表单Element
     * @param inputName   input的name
     * @param value       要输入的值
     */
    protected void inputText(WebElement formElement, String inputName, String value) {
        try {
            WebElement input = formElement.findElement(By.name(inputName));
            input.clear();
            if (value != null)
                input.sendKeys(value);
        } catch (ElementNotVisibleException exception) {
            printThisPage();
            throw exception;
        }

    }

    /**
     * 点击这个class的菜单
     *
     * @param className className
     */
    protected void clickMenuByClass(String className) {
        // 排除掉 mb30,nav-justified class 或者在 visible-xs内
        //
        List<WebElement> ulList = webDriver.findElements(By.cssSelector("ul[class~=nav]"));
        WebElement ul = null;
        for (WebElement element : ulList) {
            if (element.getAttribute("class").contains("mb30"))
                continue;
            if (element.getAttribute("class").contains("nav-justified"))
                continue;
            ul = element;
        }

        if (ul == null)
            throw new IllegalStateException("找不到必要的导航ul nav");

        List<WebElement> list = ul.findElements(By.tagName("li"));

        WebElement parentElement = null;
        for (WebElement element : list) {
            if (element.findElements(By.className(className)).isEmpty())
                continue;

            // 排除上级菜单
            if (element.getAttribute("class") != null && element.getAttribute("class").contains("nav-parent")) {
                parentElement = element;
                continue;
            }

            if (element.isDisplayed()) {
                clickElement(element);
                return;
            } else {
                if (parentElement == null)
                    throw new IllegalStateException("指定的菜单无法展示" + className);
                parentElement.click();
                clickElement(element);
            }
        }
    }

    /**
     * @param element 点击这个东西
     */
    private void clickElement(WebElement element) {
        try {
            element.findElement(By.tagName("a")).click();
        } catch (Exception ignored) {
            //noinspection EmptyCatchBlock
            try {
                element.findElement(By.tagName("button")).click();
            } catch (Exception ignored1) {
                //noinspection EmptyCatchBlock
                try {
                    element.findElement(By.tagName("img")).click();
                } catch (Exception ignored2) {
                    log.warn("找不到里面的可点击目标,将直接点击自身");
                    element.click();
                }
            }
        }
    }

    <T extends AbstractPage> T initPage(Class<T> pageClass) {
        T page = PageFactory.initElements(webDriver, pageClass);
//        page.setResourceService(resourceService);
        page.setTestInstance(getTestInstance());
        page.validatePage();
        return page;
    }

    protected void inputChecked(WebElement form, String name, boolean checked) {
        WebElement input = form.findElement(By.name(name));
        if (checked && inputChecked(input))
            return;
        if (!checked && !inputChecked(input))
            return;
        input.click();
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
    protected void assertInputChecked(WebElement form, String name, boolean checked) {
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
    protected void assertInputSelect(WebElement form, String name, String label) {
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
    protected void assertInputText(WebElement form, String name, String value) {
        WebElement input = form.findElement(By.name(name));
        assertThat(input.getAttribute("value"))
                .isEqualTo(value);
    }
}
