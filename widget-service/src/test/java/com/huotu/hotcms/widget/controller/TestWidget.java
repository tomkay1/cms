package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.test.TestBase;
import org.springframework.core.io.Resource;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hzbc on 2016/6/22.
 */
public class TestWidget  implements Widget  {
    @Override
    public String groupId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String widgetId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String name(Locale locale) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String description() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String description(Locale locale) {
        return UUID.randomUUID().toString();
    }

    @Override
    public int dependBuild() {
        return 0;
    }

    @Override
    public Map<String, Resource> publicResources() {
        return null;
    }

    @Override
    public Resource widgetJs() {
        return null;
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[0];
    }

    @Override
    public void valid(String styleId, ComponentProperties properties) throws IllegalArgumentException {

    }

    @Override
    public Class springConfigClass() {
        return null;
    }
}
