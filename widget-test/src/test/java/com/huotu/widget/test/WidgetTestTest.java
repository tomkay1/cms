/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import me.jiangcai.lib.resource.service.ResourceService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class WidgetTestTest extends WidgetTest {

    @Autowired
    private ResourceService resourceService;

    @Override
    protected boolean printPageSource() {
        return true;
    }

    @Override
    protected void editorWork(Widget widget, Editor editor, Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {
        if (driver instanceof JavascriptExecutor) {
            Boolean initFlag = (Boolean) ((JavascriptExecutor) driver).executeScript("return window['inited']");
            assertThat(initFlag)
                    .as("编辑器初始化, see demoWidget.js")
                    .isNotNull()
                    .isTrue();
        }
        editor.getWebElement().findElement(By.id("DataFetcher")).click();
        editor.chooseSerial("serial", "123444");
        Map<String, Object> map = currentWidgetProperties.get();
        Object result = map.get("DataFetcherResult");
        Object serial = map.get("serial");
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class);
        assertThat(serial).isEqualTo("123444");


    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger)
            throws IOException {
        WebElement webElement = uiChanger.apply(widget.defaultProperties(resourceService));
        WebElement widgetUrl = webElement.findElement(By.className("widgetUrl"));
        String url = widgetUrl.getAttribute("href");
        url = URLDecoder.decode(url, "utf-8");
        assertThat(url.matches(".*-.*=.*"))
                .isTrue();
    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> uiChanger
            , Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {
        WebElement webElement = uiChanger.apply(widget.defaultProperties(resourceService));
        ComponentProperties properties = widget.defaultProperties(resourceService);
        assertThat(webElement.findElement(By.name("content")).getAttribute("value"))
                .isEqualTo(properties.get("content").toString());
    }
}