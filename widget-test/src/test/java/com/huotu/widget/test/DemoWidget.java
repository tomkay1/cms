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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author CJ
 */
public class DemoWidget implements Widget {
    @Override
    public String groupId() {
        return "foo";
    }

    @Override
    public String widgetId() {
        return "bar";
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
    public int dependBuild() {
        return 0;
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String , Resource> map = new HashMap<>();
        map.put("thumbnail.png",new ClassPathResource("thumbnail.png",getClass().getClassLoader()));
        return map;
    }

    @Override
    public Resource widgetJs() {
        return null;
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new DemoStyle()};
    }

    @Override
    public void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException {

    }

    @Override
    public Class springConfigClass() {
        return null;
    }
}
