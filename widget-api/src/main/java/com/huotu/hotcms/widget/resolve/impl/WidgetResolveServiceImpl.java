/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.impl;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.page.Empty;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import com.huotu.hotcms.widget.resolve.WidgetContext;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    //    @Autowired(required = false)
    private SpringTemplateEngine widgetTemplateEngine;

    @Autowired(required = false)
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
        Map<String, org.springframework.core.io.Resource> publicResources = widget.publicResources();
        WidgetIdentifier identifier = new WidgetIdentifier(widget.groupId(), widget.widgetId(), widget.version());
        if (publicResources.containsKey(resourceName)) {
            Resource resource = resourceService.getResource("widget/" + identifier.toURIEncoded() + "/"
                    + resourceName);
            return resource.httpUrl().toURI();
        }
        return null;
    }

    @Override
    public String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties) {
        WidgetStyle style = getWidgetStyle(widget, styleId);
        checkEngine();
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, style, webApplicationContext.getServletContext(), null, properties);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.PREVIEW
                , Collections.singleton("div"), widgetContext);
    }


    @Override
    public String editorHTML(Widget widget, CMSContext cmsContext, ComponentProperties properties) {
        WidgetStyle style = widget.styles()[0];
        //构造控件专用的上下文
        checkEngine();
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                , widget, style, webApplicationContext.getServletContext(), null, properties);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
        return widgetTemplateEngine.process(WidgetTemplateResolver.EDITOR
                , Collections.singleton("div"), widgetContext);
    }

    @Override
    public void pageElementHTML(PageElement pageElement, CMSContext cmsContext, Writer writer) throws IOException {
        checkEngine();
        String className;
        if (pageElement instanceof Layout) {
            //是一个布局界面
            Layout layout = ((Layout) pageElement);
            String[] columns = layout.columns();
            PageElement[][] childPageElements = layout.getElementGroups();
            for (int i = 0, l = columns.length; i < l; i++) {
                if (Integer.parseInt(columns[i]) == 100) {
                    writer.append("<div>");
                } else if (Integer.parseInt(columns[i]) == 50) {
                    writer.append("<div class=\"").append("container").append("\">");
                } else {
                    cmsContext.updateNextBootstrapLayoutColumn(Integer.parseInt(columns[i]));
                    className = cmsContext.getNextBootstrapClass();
                    writer.append("<div class=\"").append(className).append("\">");
                }
                if (childPageElements != null && childPageElements.length >= 0 && i < childPageElements.length) {
                    for (PageElement pageElement1 : childPageElements[i]) {
                        pageElementHTML(pageElement1, cmsContext, writer);
                    }
                }
                writer.append("</div>");
            }
        } else if (pageElement instanceof Component) {
            //是一个组件
            Component component = (Component) pageElement;
            InstalledWidget installedWidget = component.getInstalledWidget() != null ? component.getInstalledWidget()
                    : widgetLocateService.findWidget(component.getWidgetIdentity());
            component.setInstalledWidget(installedWidget);
            WidgetStyle style = null;
            if (installedWidget != null) {
                style = getWidgetStyle(installedWidget.getWidget(), component.getStyleId());
            }

            WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                    , installedWidget != null ? component.getInstalledWidget().getWidget() : null, style
                    , webApplicationContext.getServletContext()
                    , component, component.getProperties());
//            RequestContextHolder.currentRequestAttributes();
//            RequestContext.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
            WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
            cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
            widgetTemplateEngine.process(WidgetTemplateResolver.BROWSE
                    , Collections.singleton("div"), widgetContext, writer);
        } else {
            //占位组件
            writer.append("<div> </div>");
        }

    }

    @Override
    public void widgetDependencyContent(CMSContext context, Widget widget, MediaType type, PageElement element
            , OutputStream out) throws IOException {
        // 必须是以安装的控件
        checkEngine();

        WidgetContext widgetContext;
        if (element == null) {
            if (widget == null)
                throw new IllegalArgumentException("both widget element is null.");
            widgetContext = new WidgetContext(widgetTemplateEngine, context
                    , widget, null, webApplicationContext.getServletContext()
                    , null, widget.defaultProperties(resourceService));
        } else if (element instanceof Empty) {
            return;
        } else if (element instanceof Layout) {
            Layout layout = ((Layout) element);
            for (PageElement pageElement1 : layout.elements()) {
                widgetDependencyContent(context, null, type, pageElement1, out);
            }
            return;
        } else {
            Component component = (Component) element;
            widget = component.getInstalledWidget().getWidget();
            widgetContext = new WidgetContext(widgetTemplateEngine, context
                    , widget, component.currentStyle(), webApplicationContext.getServletContext()
                    , component, component.getProperties());
        }

        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        context.getWidgetConfigurationStack().push(widgetConfiguration);
        widgetTemplateEngine.process(
                type.equals(Widget.CSS) ? WidgetTemplateResolver.CSS : WidgetTemplateResolver.JAVASCRIPT, widgetContext
                , new OutputStreamWriter(out, "UTF-8"));
    }

    @Override
    public String pageElementHTML(PageElement pageElement, CMSContext cmsContext) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilderWriter writer = new StringBuilderWriter(stringBuilder);
        try {
            pageElementHTML(pageElement, cmsContext, writer);
        } catch (IOException e) {
            // working in mem. so never happen
            throw new IllegalStateException("Mem IO", e);
        }
        return stringBuilder.toString();
    }

    private WidgetStyle getWidgetStyle(Widget widget, String styleId) {
        return WidgetStyle.styleByID(widget, styleId);
    }

}
