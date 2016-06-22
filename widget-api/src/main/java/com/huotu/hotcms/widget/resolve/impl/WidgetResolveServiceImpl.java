/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.impl;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import com.huotu.hotcms.widget.resolve.WidgetContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by lhx on 2016/6/21.
 */
@Service
public class WidgetResolveServiceImpl implements WidgetResolveService {
    private static final Log log = LogFactory.getLog(WidgetResolveServiceImpl.class);
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SpringTemplateEngine widgetTemplateEngine;


    @Override
    public URI resourceURI(Widget widget, String resourceName) throws URISyntaxException, IOException {
        Map<String, Resource> publicResources = widget.publicResources();
        Resource resource = publicResources.get(resourceName);
        return resource.getURI();
//        if (resource!=null)
//            return new URI(webApplicationContext.getServletContext().getRealPath("/")+"/"+resourceName);
//        return null;
    }

    @Override
    public String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties) {
        WidgetStyle style = null;
        for (WidgetStyle style1 : widget.styles()) {
            if (style1.id().equals(styleId)) {
                style = style1;
                break;
            }
        }
        if (style == null) {
            style = widget.styles()[0];
        }

        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, style, webApplicationContext.getServletContext(), properties);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.PREVIEW
                , Collections.singleton("div"), widgetContext);
    }

    @Override
    public String editorHTML(Widget widget, CMSContext cmsContext, ComponentProperties properties) {
        //构造控件专用的上下文
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, null, webApplicationContext.getServletContext(), properties);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.EDITOR
                , Collections.singleton("div"), widgetContext);
    }

    @Override
    public String componentHTML(Component component, CMSContext cmsContext) {
        WidgetStyle style = null;
        for (WidgetStyle style1 : component.getWidget().getWidget().styles()) {
            if (style1.id().equals(component.getStyleId())) {
                style = style1;
                break;
            }
        }

        if (style == null) {
            style = component.getWidget().getWidget().styles()[0];
        }

        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , component.getWidget().getWidget(), style, webApplicationContext.getServletContext(), component.getProperties());
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.BROWSE
                , Collections.singleton("div"), widgetContext);
    }
}
