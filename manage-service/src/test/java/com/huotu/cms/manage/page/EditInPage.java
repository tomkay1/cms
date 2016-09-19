/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import me.jiangcai.lib.test.page.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author CJ
 */
public class EditInPage extends AbstractPage {

    public EditInPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {

    }

    public void chooseRandomOne() {
//        System.out.println(webDriver.getPageSource());
        WebElement target = webDriver.findElements(By.className("list-group-item"))
                .stream()
                .findAny().orElseThrow(IllegalStateException::new);

        System.out.println(target);

        new Actions(webDriver)
                .click(target)
//                .moveToElement(target)
//                .doubleClick()
                .perform();
    }
}
