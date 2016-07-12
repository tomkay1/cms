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
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import com.huotu.hotcms.widget.resolve.WidgetContext;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

/**
 * 此处如果选择依赖{@link SpringTemplateEngine widgetTemplateEngine}必然会形成循环依赖
 * Created by lhx on 2016/6/21.
 */
@Service
public class WidgetResolveServiceImpl implements WidgetResolveService {
    private static final Log log = LogFactory.getLog(WidgetResolveServiceImpl.class);
    @Autowired
    private WebApplicationContext webApplicationContext;

    //    @Autowired
    private SpringTemplateEngine widgetTemplateEngine;

    @Autowired
    private WidgetLocateService widgetLocateService;

    @Autowired
    private ResourceService resourceService;


    private void checkEngine() {
        if (widgetTemplateEngine == null) {
            widgetTemplateEngine = webApplicationContext.getBean("widgetTemplateEngine", SpringTemplateEngine.class);
        }
    }

    @Override
    public URI resourceURI(Widget widget, String resourceName) throws URISyntaxException, IOException {
        Map<String, Resource> publicResources = widget.publicResources();
        if (publicResources.containsKey(resourceName)) {
            Resource resource = resourceService.getResource("widget/" + widget.groupId() + widget.widgetId()
                    + widget.version() + "/" + resourceName);
            return resource.getURI();
        }
        return null;
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
        checkEngine();
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, style, webApplicationContext.getServletContext(), properties, null);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.PREVIEW
                , Collections.singleton("div"), widgetContext);
    }

    @Override
    public String editorHTML(Widget widget, CMSContext cmsContext, ComponentProperties properties) {
        //构造控件专用的上下文
        checkEngine();
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, null, webApplicationContext.getServletContext(), properties, null);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.EDITOR
                , Collections.singleton("div"), widgetContext);
    }


    @Override
    public String pageElementHTML(PageElement pageElement, CMSContext cmsContext) {
        String className;
        if (pageElement instanceof Layout) {
            //是一个布局界面
            Layout layout = ((Layout) pageElement);
            String[] columns = layout.getValue().split(",");
            String html = "";
            PageElement[] childPageElements = layout.getElements();
            for (int i = 0, l = columns.length; i < l; i++) {
                cmsContext.updateNextBootstrapLayoutColumn(Integer.parseInt(columns[i]));
                className = cmsContext.getNextBootstrapClass();
                html = "<div class=\"" + className + "\">";
                if (childPageElements != null && childPageElements.length >= 0 && i < childPageElements.length) {
                    html += pageElementHTML(childPageElements[i], cmsContext);
                }
                html += "</div>";
            }
            return html;

        } else if (pageElement instanceof Component) {
            //是一个组件
            Component component = (Component) pageElement;
            InstalledWidget installedWidget = widgetLocateService.findWidget(component.getWidgetIdentity());
            component.setInstalledWidget(installedWidget);

            WidgetStyle style = null;
            for (WidgetStyle style1 : component.getInstalledWidget().getWidget().styles()) {
                if (style1.id().equals(component.getStyleId())) {
                    style = style1;
                    break;
                }
            }
            if (style == null) {
                style = component.getInstalledWidget().getWidget().styles()[0];
            }

            checkEngine();
            WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                    , component.getInstalledWidget().getWidget(), style, webApplicationContext.getServletContext()
                    , component.getProperties(), component.getStyleClassNames());
            WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
            cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
            return widgetTemplateEngine.process(WidgetTemplateResolver.BROWSE
                    , Collections.singleton("div"), widgetContext);
        } else {
            //占位组件
            return "<div> </div>";
        }

    }

    @Override
    public void componentCSS(CMSContext cmsContext, PageElement pageElement, OutputStream out) throws IOException {
        if (pageElement instanceof Layout) {
            //是一个布局界面
            Layout layout = ((Layout) pageElement);
            String[] columns = layout.getValue().split(",");
            PageElement[] childPageElements = layout.getElements();
            for (int i = 0, l = columns.length; i < l; i++) {
                if (childPageElements != null && childPageElements.length >= 0 && i < childPageElements.length) {
                    componentCSS(cmsContext, childPageElements[i], out);
                }
            }
        } else if (pageElement instanceof Component) {
            //是一个组件
            Component component = (Component) pageElement;
            InstalledWidget installedWidget = widgetLocateService.findWidget(component.getWidgetIdentity());
            component.setInstalledWidget(installedWidget);
            if (installedWidget.getWidget().widgetDependencyContent(ContentType.create("text/css")) != null) {
                WidgetStyle style = null;
                for (WidgetStyle style1 : component.getInstalledWidget().getWidget().styles()) {
                    if (style1.id().equals(component.getStyleId())) {
                        style = style1;
                        break;
                    }
                }
                if (style == null) {
                    style = component.getInstalledWidget().getWidget().styles()[0];
                }
                checkEngine();
                WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                        , component.getInstalledWidget().getWidget(), style, webApplicationContext.getServletContext()
                        , component.getProperties(), component.getStyleClassNames());
                WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
                cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
                String css = widgetTemplateEngine.process(WidgetTemplateResolver.CSS, widgetContext);
                out.write(css.getBytes(), 0, css.getBytes().length);
            }
        }
    }


}
