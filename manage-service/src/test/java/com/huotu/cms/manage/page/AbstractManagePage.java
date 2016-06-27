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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author CJ
 */
public abstract class AbstractManagePage extends BracketPage {

    public AbstractManagePage(WebDriver webDriver) {
        super(webDriver);
    }

    protected void inputSelect(WebElement formElement, String inputName, String label) {
        WebElement input = formElement.findElement(By.name(inputName));
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
        WebElement input = formElement.findElement(By.name(inputName));
        input.clear();
        if (value != null)
            input.sendKeys(value);
    }
}
