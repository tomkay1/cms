/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.test;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hzbc on 2016/6/22.
 */
public class TestWidget implements Widget  {
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
    public Resource widgetDependencyContent(MediaType contentType) {
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

    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException {
        return null;
    }

}
