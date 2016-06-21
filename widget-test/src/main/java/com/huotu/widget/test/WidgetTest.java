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
import com.huotu.widget.test.bean.WidgetHolder;
import com.huotu.widget.test.bean.WidgetViewController;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author CJ
 */
@ContextConfiguration(classes = WidgetTestConfig.class)
@WebAppConfiguration
public abstract class WidgetTest extends SpringWebTest {

    @Autowired
    private WidgetViewController widgetViewController;

    @Autowired
    private WidgetHolder holder;

    /**
     * @return true to open pageSource
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract boolean printPageSource();

    /**
     * 编辑器测试
     * 总体测试流程
     * 打开编辑器
     * 执行编辑操作
     * 校验编辑结果
     */
    @Test
    public void editor() throws Exception {
        for (Widget widget : holder.getWidgetSet()) {
            editor(widget);
        }
    }

    @Test
    public void style() {
        for (Widget widget : holder.getWidgetSet()) {
            assertThat(widget.styles())
                    .isNotNull();
            assertThat(widget.styles().length)
                    .isGreaterThanOrEqualTo(1);

            for (WidgetStyle style : widget.styles()) {
                stylePropertiesFor(style);
                browseWork(widget, style, componentProperties -> {
                    widgetViewController.setCurrentProperties(componentProperties);
                    String uri = "/browse/" + WidgetTestConfig.WidgetIdentity(widget) + "/" + style.id();
                    if (printPageSource())
                        try {
                            mockMvc.perform(get(uri))
                                    .andDo(print());
                        } catch (Exception e) {
                            throw new IllegalStateException("no print html");
                        }
                    driver.get("http://localhost" + uri);
                    WebElement webElement = driver.findElement(By.id("browse")).findElement(By.tagName("div"));
                    return webElement;
                });
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void editor(Widget widget) throws Exception {

        if (printPageSource())
            mockMvc.perform(get("/editor/" + WidgetTestConfig.WidgetIdentity(widget)))
                    .andDo(print());

        driver.get("http://localhost/editor/" + WidgetTestConfig.WidgetIdentity(widget));
        editorWork(widget, driver.findElement(By.id("editor")).findElement(By.tagName("div")), () -> {
            if (driver instanceof JavascriptExecutor) {
                return (Map) ((JavascriptExecutor) driver).executeScript("return widgetProperties($('#editor'))");
            }
            throw new IllegalStateException("no JavascriptExecutor driver");
        });
    }

    /**
     * 执行编辑操作,校验编辑结果
     * {@link #driver}应该是一个{@link JavascriptExecutor}
     * 可以通过这个方法获取脚本信息
     *
     * @param widget                  控件
     * @param editor                  编辑器element
     * @param currentWidgetProperties 可以从浏览器中获取当前控件属性
     * @see JavascriptExecutor#executeScript(String, Object...)
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract void editorWork(Widget widget, WebElement editor
            , Supplier<Map<String, Object>> currentWidgetProperties);

    /**
     * 浏览视图的测试
     * 通过设置属性改变预览视图
     * @param widget    控件
     * @param style     样式
     * @param uiChanger 更改后的预览视图
     */
    protected abstract void browseWork(Widget widget, WidgetStyle style
            , Function<ComponentProperties, WebElement> uiChanger);

    /**
     * 一些常用属性测试
     *
     * @throws IOException
     */
    @Test
    public void properties() throws IOException {
        holder.getWidgetSet().forEach(this::propertiesFor);
    }

    @SuppressWarnings("WeakerAccess")
    protected void propertiesFor(Widget widget) {


        assertThat(widget.description())
                .isNotEmpty();

        assertThat(widget.groupId())
                .isNotEmpty();

        assertThat(widget.widgetId())
                .isNotEmpty();


        if (widget.publicResources() != null) {
            widget.publicResources().forEach((name, resource) -> {
                assertThat(name)
                        .as("公开资源名字不可为空")
                        .isNotNull();
                assertThat(resource)
                        .as("公开资源内容不可为空")
                        .isNotNull();
                assertThat(resource.isReadable())
                        .as("公开资源需可读")
                        .isTrue();
                assertThat(resource.exists())
                        .as("公开资源必须存在")
                        .isTrue();
            });
        }

        assertThat(widget.thumbnail())
                .isNotNull();
        assertThat(widget.thumbnail().isReadable())
                .as("必须拥有缩略图")
                .isTrue();
        //图片资源必须是 106x82 png
        try {
            BufferedImage thumbnail = ImageIO.read(widget.thumbnail().getInputStream());
            assertThat((float) thumbnail.getWidth() / (float) thumbnail.getHeight())
                    .as("缩略图必须为106*82的PNG图片")
                    .isEqualTo(106f / 82f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // 现在检查编辑器
    }


    /**
     * 样式基本属性测试
     * @param widgetStyle
     */
    protected void stylePropertiesFor(WidgetStyle widgetStyle) {
        assertThat(widgetStyle.description())
                .isNotEmpty();

        assertThat(widgetStyle.id())
                .isNotEmpty();

        assertThat(widgetStyle.name())
                .isNotEmpty();
        assertThat(widgetStyle.thumbnail())
                .as("缩略图不可为空")
                .isNotNull();
        assertThat(widgetStyle.thumbnail().exists())
                .as("公开资源必须存在")
                .isTrue();

        //图片资源必须是 106x82 png
        try {
            BufferedImage thumbnail = ImageIO.read(widgetStyle.thumbnail().getInputStream());
//            assertThat((float) thumbnail.getWidth() / (float) thumbnail.getHeight())
//                    .as("缩略图必须为106*82的PNG图片")
//                    .isEqualTo(106f / 82f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // 现在检查编辑器
    }
}
