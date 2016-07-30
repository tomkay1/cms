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
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import com.huotu.hotcms.widget.resolve.WidgetContext;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
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
        String className;
        if (pageElement instanceof Layout) {
            //是一个布局界面
            Layout layout = ((Layout) pageElement);
            String[] columns = layout.columns();
            PageElement[][] childPageElements = layout.getElementGroups();
            for (int i = 0, l = columns.length; i < l; i++) {
                cmsContext.updateNextBootstrapLayoutColumn(Integer.parseInt(columns[i]));
                className = cmsContext.getNextBootstrapClass();
                writer.append("<div class=\"").append(className).append("\">");
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
    public String widgetJavascript(CMSContext context, Widget widget) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            widgetJavascript(context, widget, buf);
            return buf.toString("UTF-8");
        } catch (IOException e) {
            // working in mem. so never happen
            throw new IllegalStateException("Mem IO", e);
        }
    }

    @Override
    public void widgetJavascript(CMSContext context, Widget widget, OutputStream out) throws IOException {
        // 必须是以安装的控件
        checkEngine();
        WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, context
                , widget, null, webApplicationContext.getServletContext()
                , null, null);
        WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
        context.getWidgetConfigurationStack().push(widgetConfiguration);
        widgetTemplateEngine.process(WidgetTemplateResolver.JAVASCRIPT, widgetContext
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

    @Override
    public void componentCSS(CMSContext cmsContext, PageElement pageElement, OutputStream out) throws IOException {
        if (pageElement instanceof Layout) {
            //是一个布局界面
            Layout layout = ((Layout) pageElement);
            for (PageElement pageElement1 : layout.elements()) {
                componentCSS(cmsContext, pageElement1, out);
            }
        } else if (pageElement instanceof Component) {
            //是一个组件
            Component component = (Component) pageElement;
            InstalledWidget installedWidget = widgetLocateService.findWidget(component.getWidgetIdentity());
            component.setInstalledWidget(installedWidget);
            if (installedWidget != null &&
                    installedWidget.getWidget().widgetDependencyContent(ContentType.create("text/css")) != null) {
                WidgetStyle style = getWidgetStyle(component.getInstalledWidget().getWidget(), component.getStyleId());
                checkEngine();
                WidgetContext widgetContext = new WidgetContext(widgetTemplateEngine, cmsContext
                        , component.getInstalledWidget().getWidget(), style, webApplicationContext.getServletContext()
                        , component, component.getProperties());
                WidgetConfiguration widgetConfiguration = (WidgetConfiguration) widgetContext.getConfiguration();
                cmsContext.getWidgetConfigurationStack().push(widgetConfiguration);
                widgetTemplateEngine.process(WidgetTemplateResolver.CSS, widgetContext
                        , new OutputStreamWriter(out, "UTF-8"));
//                out.write(css.getBytes(), 0, css.getBytes().length);
            }
        }
    }


    private WidgetStyle getWidgetStyle(Widget widget, String styleId) {
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
        return style;
    }

}
