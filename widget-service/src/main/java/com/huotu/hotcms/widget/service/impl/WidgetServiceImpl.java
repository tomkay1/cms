/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.widget.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * Created by elvis on 2016/6/7.
 */
public class WidgetServiceImpl implements WidgetService {

    @Autowired
    ApplicationContext applicationContext;


    @Override
    public URI resourceURI(Widget widget, String resourceName) {
        Map<String ,Resource> publicResources = widget.publicResources();
        Resource resource = publicResources.get(resourceName);
        try {
            return resource.getURI();
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties, WebEngineContext context) {
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        String finalHTML = engine.process("TEMPLATE/" +  ((Widget) context.getVariable("widget")).widgetId().replace('-', '.')
                + "/" + styleId , Collections.singleton("div"), context);
        return finalHTML;
    }

    @Override
    public String editorHTML(Widget widget, CMSContext cmsContext, WebEngineContext context) {
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        String finalHTML = engine.process("EDITOR/"+ ((Widget) context.getVariable("widget")).widgetId().replace('-', '.')
                , Collections.singleton("div"), context);
        return finalHTML;
    }

    @Override
    public String componentHTML(Component component, CMSContext cmsContext, WebEngineContext context) {
        Widget widget =  component.getWidget().getWidget();
        String styleId = component.getStyleId();
        ComponentProperties componentProperties = component.getProperties();
        return null;
    }
}
