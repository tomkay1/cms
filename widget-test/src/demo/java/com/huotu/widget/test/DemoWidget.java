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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author CJ
 */
public class DemoWidget implements Widget {
    @Override
    public String groupId() {
        return "com.huotu.foo";
    }

    @Override
    public String widgetId() {
        return "bar-os";
    }

    @Override
    public String name(Locale locale) {
        return "hello";
    }

    @Override
    public String description() {
        return "hello world";
    }

    @Override
    public String description(Locale locale) {
        return "Hello World";
    }

    @Override
    public String dependVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail.png", new ClassPathResource("thumbnail.png", getClass().getClassLoader()));
        map.put("other/thumbnail.png", new ClassPathResource("thumbnail.png", getClass().getClassLoader()));
        return map;
    }

    @Override
    public Resource widgetDependencyContent(MediaType contentType) {
        if (contentType.equals(Javascript)) {
            return new ClassPathResource("js/demoWidget.js");
        }
        if (contentType.equals(CSS)) {
            return new ClassPathResource("css/demo.css");
        }
        return null;
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new DemoStyle()};
    }

    @Override
    public void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException {
        WidgetStyle style = WidgetStyle.styleByID(this, styleId);
        if (!properties.containsKey("content")) {
            throw new IllegalArgumentException("参数异常");
        }

    }

    @Override
    public Class springConfigClass() {
        return null;
    }

    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) {
        ComponentProperties properties = new ComponentProperties();
        properties.put("content", "lalalalala!!!");
        return properties;
    }
}
