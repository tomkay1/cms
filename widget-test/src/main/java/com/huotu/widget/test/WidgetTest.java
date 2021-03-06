/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.bean.WidgetHolder;
import com.huotu.hotcms.bean.WidgetViewController;
import com.huotu.hotcms.hold.TestWidgetHolder;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author CJ
 */
@ContextConfiguration(classes = {WidgetTestConfig.class, WidgetTest.WidgetLoader.class})
@WebAppConfiguration
@Transactional
public abstract class WidgetTest extends SpringWebTest {

    @Autowired(required = false)
    public ResourceService resourceService;
    @Autowired
    HttpServletResponse response;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private WidgetViewController widgetViewController;
    @Autowired
    private WidgetHolder holder;

    public void defaultEditor() {

    }

    public void randomCategory(Site site) {
        Category category = new Category();
        category.setSite(site);
        category.setCreateTime(LocalDateTime.now());
        category.setSerial("123");
        category.setContentType(ContentType.Article);
        category = categoryRepository.save(category);
        Category category1 = new Category();
        category1.setSite(site);
        category1.setCreateTime(LocalDateTime.now());
        category1.setSerial("123");
        category1.setContentType(ContentType.Article);
        category1.setParent(category);
        categoryRepository.save(category1);
        Category category2 = new Category();
        category2.setSite(site);
        category2.setCreateTime(LocalDateTime.now());
        category2.setSerial("123");
        category2.setContentType(ContentType.Article);
        category2.setParent(category);
        categoryRepository.save(category2);
    }

    @Override
    public void createMockMVC() {
        MockitoAnnotations.initMocks(this);
        // ignore it, so it works in no-web fine.
        if (context == null)
            return;
        CMSContext.PutContext(request, response, siteRepository.findByRecommendDomain("localhost"));
        randomCategory(siteRepository.findByRecommendDomain("localhost"));
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        CMSFilter cmsFilter = new CMSFilter();
        cmsFilter.setServletContext(context.getServletContext());
        builder.addFilter(cmsFilter);
//        if (springSecurityFilter != null) {
//            builder = builder.addFilters(springSecurityFilter);
//        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
    }

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

    /**
     * 编辑器浏览测试
     * 打开编辑器
     * 校验编辑浏览结果
     */
    @Test
    public void editorBrowse() throws Exception {
        for (Widget widget : holder.getWidgetSet()) {
            editorBrowse(widget);
        }
    }

    @Test
    public void style() throws IOException {
        for (Widget widget : holder.getWidgetSet()) {
            assertThat(widget.styles())
                    .isNotNull();
            assertThat(widget.styles().length)
                    .isGreaterThanOrEqualTo(1);

            for (WidgetStyle style : widget.styles()) {
                stylePropertiesFor(style);
                finalBrowseWork(widget, style, componentProperties -> {
                    widgetViewController.setCurrentProperties(componentProperties);
                    String uri = "/browse/" + Widget.URIEncodedWidgetIdentity(widget) + "/" + style.id();
                    if (printPageSource())
                        try {
                            mockMvc.perform(get(uri))
                                    .andDo(print());
                        } catch (Exception e) {
                            throw new IllegalStateException("no print html");
                        }
                    driver.get("http://localhost" + uri);
                    return driver.findElement(By.id("browse")).findElement(By.tagName("div"));
                });
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void editor(Widget widget) throws Exception {
        widgetViewController.setCurrentProperties(widget.defaultProperties(resourceService));
        if (printPageSource())
            mockMvc.perform(get("/editor/" + Widget.URIEncodedWidgetIdentity(widget)))
                    .andDo(print());
        driver.get("http://localhost/editor/" + Widget.URIEncodedWidgetIdentity(widget));
        driver.findElement(By.id("editorInit")).click();
        finalEditorWork(widget, new Editor(driver.findElement(By.id("editor")).findElement(By.tagName("div")), driver), () -> {

            driver.findElement(By.id("editorSaver")).click();
            if (driver instanceof JavascriptExecutor) {
                String failedMessage = (String) ((JavascriptExecutor) driver).executeScript("return _failedMessage");
                if (failedMessage != null)
                    throw new IllegalStateException(failedMessage);
            }
            if (driver instanceof JavascriptExecutor) {
                return (Map) ((JavascriptExecutor) driver).executeScript("return _successProperties");
            }
            throw new IllegalStateException("no JavascriptExecutor driver");
        });
    }

    @SuppressWarnings("WeakerAccess")
    protected void editorBrowse(Widget widget) throws Exception {
        for (WidgetStyle style : widget.styles()) {
            stylePropertiesFor(style);
            editorBrowseWork(widget, componentProperties -> {
                widgetViewController.setCurrentProperties(componentProperties);
                String uri = "/editor/" + Widget.URIEncodedWidgetIdentity(widget);
                if (printPageSource())
                    try {
                        mockMvc.perform(get(uri))
                                .andDo(print());
                    } catch (Exception e) {
                        throw new IllegalStateException("no print html");
                    }
                driver.get("http://localhost" + uri);
                return driver.findElement(By.id("editor")).findElement(By.tagName("div"));
            }, () -> {

                driver.findElement(By.id("editorSaver")).click();
                if (driver instanceof JavascriptExecutor) {
                    String failedMessage = (String) ((JavascriptExecutor) driver).executeScript("return _failedMessage");
                    if (failedMessage != null)
                        throw new IllegalStateException(failedMessage);
                }
                if (driver instanceof JavascriptExecutor) {
                    return (Map) ((JavascriptExecutor) driver).executeScript("return _successProperties");
                }

                throw new IllegalStateException("no JavascriptExecutor driver");
            });
        }
    }

    private void finalEditorWork(Widget widget, Editor editor
            , Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {
        // 获取默认属性
//        Map<String, Object> map = currentWidgetProperties.get();
//        ComponentProperties componentProperties = new ComponentProperties();
//        componentProperties.putAll(map);
//        widget.valid(null, componentProperties);
        editorWork(widget, editor, currentWidgetProperties);
    }

    /**
     * 执行编辑操作,校验编辑结果
     * {@link #driver}应该是一个{@link JavascriptExecutor}
     * 可以通过这个方法获取脚本信息
     *
     * @param widget                  控件
     * @param editor                  编辑器element
     * @param currentWidgetProperties 可以从浏览器中获取当前控件属性,如果当前属性不被接收调用这个supplier会抛出IllegalStateException
     * @see JavascriptExecutor#executeScript(String, Object...)
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract void editorWork(Widget widget, Editor editor
            , Supplier<Map<String, Object>> currentWidgetProperties) throws IOException;

    private void finalBrowseWork(Widget widget, WidgetStyle style
            , Function<ComponentProperties, WebElement> uiChanger) throws IOException {
//        WebElement defaultWeb = uiChanger.apply(widget.defaultProperties(resourceService));
//        assertThat(defaultWeb.isDisplayed()).isTrue();
        browseWork(widget, style, uiChanger);
    }

    /**
     * 浏览视图的测试
     * 通过设置属性改变预览视图
     *
     * @param widget    控件
     * @param style     样式
     * @param uiChanger 更改后的预览视图,它接受的参数就是组件的实际properties不再需要很古怪再设置properties了
     */
    protected abstract void browseWork(Widget widget, WidgetStyle style
            , Function<ComponentProperties, WebElement> uiChanger) throws IOException;

    /**
     * 编辑器浏览测试
     * 通过设置属性改变编辑器浏览视图
     *
     * @param widget                  控件
     * @param uiChanger               更改后的编辑器浏览视图,它接受的参数就是组件的实际properties
     * @param currentWidgetProperties 可以从浏览器中获取当前控件属性,如果当前属性不被接收调用这个supplier会抛出IllegalStateException
     * @see JavascriptExecutor#executeScript(String, Object...)
     */
    protected abstract void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> uiChanger
            , Supplier<Map<String, Object>> currentWidgetProperties) throws IOException;

    /**
     * 一些常用属性测试
     *
     * @throws IOException
     */
    @Test
    public void properties() throws IOException {
        holder.getWidgetSet().forEach((widget) -> {
            try {
                propertiesFor(widget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    protected void propertiesFor(Widget widget) throws IOException {


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

        // 默认属性的测试
        ComponentProperties componentProperties = widget.defaultProperties(resourceService);
        assertThat(componentProperties)
                .as("控件默认属性")
                .isNotNull();

        // 属性 只可以有Number String Map 或者List Array
        componentPropertiesCheck(componentProperties);

        // 通过给分发用属性增加一个额外属性 再跟原始值比较;很显然不应该影响
        String randomKey = randomEmailAddress();
        String randomValue = randomEmailAddress();
        componentProperties.put(randomKey, randomValue);
        assertThat(widget.defaultProperties(resourceService).get(randomKey))
                .as("对默认属性的修改不会影响全局")
                .isNotEqualTo(randomValue);

        // 默认样式必然是可以valid的
        widget.valid(null, componentProperties);
        for (WidgetStyle style : widget.styles()) {
            widget.valid(style.id(), componentProperties);
        }

        // 缩略图测试
        assertThat(widget.thumbnail())
                .isNotNull();
        assertThat(widget.thumbnail().isReadable())
                .as("必须拥有缩略图")
                .isTrue();
        //图片资源必须是 106x82 png
//        try {
//            BufferedImage thumbnail = ImageIO.read(widget.thumbnail().getInputStream());
//            assertThat((float) thumbnail.getWidth() / (float) thumbnail.getHeight())
//                    .as("缩略图必须为106*82的PNG图片")
//                    .isEqualTo(106f / 82f);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // 现在检查编辑器
    }

    @SuppressWarnings("unchecked")
    private void componentPropertiesCheck(Object var) {
        if (var == null)
            return;
        if (var instanceof Map) {
            ((Map) var).values().forEach(this::componentPropertiesCheck);
            return;
        }
        if (var instanceof Collection) {
            ((Collection) var).forEach(this::componentPropertiesCheck);
            return;
        }
        // array
        if (var.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(var); i++) {
                componentPropertiesCheck(Array.get(var, i));
            }
            return;
        }
        if (var instanceof String)
            return;
        if (var instanceof Number)
            return;
        throw new AssertionError("不期望的控件属性:" + var);
    }

    /**
     * 样式基本属性测试
     *
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
            assertThat(thumbnail)
                    .as("缩略图必须为106*82的PNG图片").isNotNull();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // 现在检查编辑器
    }

    public static class WidgetLoader {
        @Bean
        public WidgetHolder widgetHolder() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
            return new TestWidgetHolder();
        }
    }
}
