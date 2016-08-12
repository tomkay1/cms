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
    protected void editorWork(Widget widget, WebElement editor, Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {
        Map<String, Object> map = currentWidgetProperties.get();
        assertThat(map.containsKey("content")).isEqualTo(true);
        assertThat(map.get("content")).isEqualTo(widget.defaultProperties(resourceService).get("content"));

        if (driver instanceof JavascriptExecutor) {
            Boolean initFlag = (Boolean) ((JavascriptExecutor) driver).executeScript("return window['inited']");
            assertThat(initFlag)
                    .as("编辑器初始化, see demoWidget.js")
                    .isNotNull()
                    .isTrue();
        }

        editor.findElement(By.id("DataFetcher")).click();

        Object result = currentWidgetProperties.get().get("DataFetcherResult");
        assertThat(result)
                .isNotNull()
                .isInstanceOf(List.class);
    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger) throws IOException {
        uiChanger.apply(widget.defaultProperties(resourceService));
    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> uiChanger) throws IOException {
        uiChanger.apply(widget.defaultProperties(resourceService));

    }
}