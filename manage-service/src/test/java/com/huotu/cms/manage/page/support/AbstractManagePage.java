/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page.support;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import me.jiangcai.bracket.test.BracketPage;
import me.jiangcai.lib.test.page.AbstractPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.AbstractListAssert;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractManagePage extends BracketPage {

    private static final Log log = LogFactory.getLog(AbstractManagePage.class);

    protected AbstractManagePage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * 在操作webdriver之前 必须调用
     */
    protected abstract void beforeDriver();

    public void printThisPage() {
        beforeDriver();
        System.err.println("url:" + webDriver.getCurrentUrl());
        System.err.println("page:" + this);
        System.err.println(webDriver.getPageSource());
    }

    public void assertNoDanger() throws InterruptedException {
        // 在iframe 页面体系中 错误是由top发起的。所以需要先
        Thread.sleep(100);
        List<String> messages = getGritterMessage("growl-danger");
        assertThat(messages)
                .isEmpty();
    }

    public AbstractListAssert<?, ? extends List<? extends String>, String> assertDanger() throws InterruptedException {
        Thread.sleep(100);
        List<String> messages = getGritterMessage("growl-danger");
        return assertThat(messages);
    }

    public void closeDanger() throws InterruptedException {
        closeGritterMessage("growl-danger");
    }

    private void closeGritterMessage(String typeClass) throws InterruptedException {
        while (true) {
            List<WebElement> elements = webDriver.findElements(By.className(typeClass));
            if (elements.isEmpty())
                break;
            for (WebElement message : elements) {
                WebElement close = message.findElement(By.className("gritter-close"));
                if (close.isDisplayed())
                    close.click();
            }
            Thread.sleep(100);
        }

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

    public void inputSelect(WebElement formElement, String inputName, String label) {
        WebElement input = formElement.findElement(By.name(inputName));

        if (input.getAttribute("class") != null && input.getAttribute("class").contains("chosen-select")) {
            // 换一个方式
            WebElement container = formElement.findElements(By.className("chosen-container"))
                    .stream()
                    .filter(webElement -> webElement.getAttribute("title") != null && webElement.getAttribute("title")
                            .equals(input.getAttribute("title")))
                    .findAny().orElseThrow(() -> new IllegalStateException("使用了chosen-select,但没看到chosen-container"));

            container.click();
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

    public void assertInputTags(WebElement form, String name, Iterable<String> tags) {
        WebElement input = form.findElement(By.name(name));
        String id = input.getAttribute("id");

        WebElement div = form.findElement(By.id(id + "_tagsinput"));

        assertThat(div.findElements(By.className("tag")).stream()
                .map((tag) -> tag.findElement(By.tagName("span")))
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList())
        ).containsOnlyElementsOf(tags);

    }

    public void inputTags(WebElement formElement, String inputName, String[] values) {
        beforeDriver();
        Supplier<String> idSupplier = () -> formElement.findElement(By.name(inputName)).getAttribute("id");
        // 规律是加上 _tag

        // 结构为
        //
//        <div id="id_tagsinput" class="tagsinput">
//                <span class="tag"><span>...</span><a>...</a></span>
//                <span class="tag"><span>...</span><a>...</a></span>
//                ...
//                <span class="tag"><span>...</span><a>...</a></span>
//                <div id="id_addTag">
//                    <input id="id_tag" />
//                </div>
//        </div>
        while (true) {
            WebElement div = formElement.findElement(By.id(idSupplier.get() + "_tagsinput"));
            if (div.findElements(By.className("tag")).isEmpty())
                break;
            div.findElement(By.className("tag")).findElement(By.tagName("a")).click();
        }
//        for (WebElement tag : div.findElements(By.className("tag"))) {
//            tag.findElement(By.tagName("a")).click();
//        }

        for (String value : values) {
            System.out.println("_____" + idSupplier.get() + "_tag" + "   " + formElement);
            WebElement toInput = formElement.findElement(By.id(idSupplier.get() + "_tag"));
            toInput.clear();
            toInput.sendKeys(value);
//            toInput.sendKeys(Keys.ENTER);
            formElement.findElement(By.id(idSupplier.get() + "_tagsinput")).click();
        }
    }

    /**
     * 在指定表单中输入值type=text
     *
     * @param formElement 表单Element
     * @param inputName   input的name
     * @param value       要输入的值
     */
    public void inputText(WebElement formElement, String inputName, String value) {
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
//                new Actions(webDriver)
//                .moveToElement(parentElement)
//                .click()
//                .moveToElement(element)
//                .click()
//                .build()
//                .perform();
//                System.out.println("1");
                clickElement(parentElement);
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
        } catch (NoSuchElementException ignored) {
            //noinspection EmptyCatchBlock
            try {
                element.findElement(By.tagName("button")).click();
            } catch (NoSuchElementException ignored1) {
                //noinspection EmptyCatchBlock
                try {
                    element.findElement(By.tagName("img")).click();
                } catch (NoSuchElementException ignored2) {
                    log.warn("找不到里面的可点击目标,将直接点击自身", ignored2);
//                    printThisPage();
                    element.click();
                }
            }
        }
    }

    protected <T extends AbstractPage> T initPage(Class<T> pageClass) {
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
    public void assertInputChecked(WebElement form, String name, boolean checked) {
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
    public void assertInputSelect(WebElement form, String name, String label) {
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
            assertThat(new Select(input).getFirstSelectedOption().getText())
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
    public void assertInputText(WebElement form, String name, String value) {
        WebElement input = form.findElement(By.name(name));
        assertThat(input.getAttribute("value"))
                .isEqualTo(value);
    }

    public void assertInputTextarea(WebElement form, String name, String value) {
        WebElement input = form.findElement(By.name(name));
        assertThat(input.getText())
                .isEqualTo(value);
    }

    public void inputHidden(WebElement form, String name, String value) {
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

    /**
     * 上传文件
     *
     * @param form     成功后将值写入的form
     * @param name     隐藏字段的名称
     * @param resource 需要上传的资源
     */
    public void uploadResource(WebElement form, String name, Resource resource) {

    }

}
