/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 将几个常用动作 写成单独文件
 * <p>
 * JUnit测试是单线程的,所以这个Controller不用考虑并发问题,所以该实例可以设计未非线程安全
 *
 * @author CJ
 */
@Controller
public class WidgetViewController {

    private static final Log log = LogFactory.getLog(WidgetViewController.class);

    @Autowired
    private WidgetLocateService widgetLocateService;
    @Autowired
    private WidgetResolveService widgetResolveService;


    private ComponentProperties currentProperties;

    @Autowired
    private ResourceService resourceService;

    public ComponentProperties getCurrentProperties() {
        return currentProperties;
    }

    public void setCurrentProperties(ComponentProperties currentProperties) {
        this.currentProperties = currentProperties;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/css/{widgetName}.css")
    public void css(@PathVariable("widgetName") WidgetIdentifier widgetName, HttpServletResponse response) throws IOException {
        InstalledWidget widget = widgetLocateService.findWidget(widgetName.getGroupId(), widgetName.getArtifactId()
                , widgetName.getVersion());

        Component component = new Component();
        component.setInstalledWidget(widget);
        component.setWidgetIdentity(Widget.WidgetIdentity(widget.getWidget()));
        component.setProperties(currentProperties);

        response.setContentType("text/css");
        widgetResolveService.widgetDependencyContent(CMSContext.RequestContext(), widget.getWidget(), Widget.CSS
                , component, response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/editor/{widgetName}")
    public String editor(@PathVariable("widgetName") WidgetIdentifier widgetName, Model model) {
        model.addAttribute("widgetId", widgetName.toString());
        return "editor";
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/browse/{widgetName}/{styleId}"})
    public String browse(@PathVariable("widgetName") WidgetIdentifier widgetName
            , @PathVariable("styleId") String styleId, Model model) {
        InstalledWidget installedWidget = widgetLocateService.findWidget(widgetName.toString());

        Component component = new Component();
        component.setProperties(currentProperties);
        component.setId(UUID.randomUUID().toString());
        component.setStyleId(styleId);
        component.setInstalledWidget(installedWidget);
        if (installedWidget != null) {
            model.addAttribute("widget", installedWidget.getWidget());
            for (WidgetStyle style : installedWidget.getWidget().styles()) {
                if (style.id().equals(styleId)) {
                    model.addAttribute("style", installedWidget.getWidget().styles()[0]);
                    break;
                }
            }
        }
        model.addAttribute("component", component);
        model.addAttribute("widgetURLEDId", Widget.URIEncodedWidgetIdentity(installedWidget.getWidget()));
        return "browse";
    }


//    @RequestMapping(method = RequestMethod.GET, value = {"/javascript/{id}"}, produces = "application/javascript")
//    public String javascript(@PathVariable("id") String widgetId) throws IOException {
//        InstalledWidget installedWidget = widgetLocateService.findWidget(widgetId);
//        resourceService.uploadResource("testWidget/js/" + widgetId + ".js"
//                , installedWidget.getWidget().widgetJs().getInputStream());
//        return widgetId + ".js";
//    }


}
