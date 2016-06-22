/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetLocateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 将几个常用动作 写成单独文件
 *
 * JUnit测试是单线程的,所以这个Controller不用考虑并发问题,所以该实例可以设计未非线程安全
 * @author CJ
 */
@Controller
public class WidgetViewController {

    private static final Log log = LogFactory.getLog(WidgetViewController.class);

    @Autowired
    private WidgetLocateService widgetLocateService;

    private ComponentProperties currentProperties;

    public ComponentProperties getCurrentProperties() {
        return currentProperties;
    }

    public void setCurrentProperties(ComponentProperties currentProperties) {
        this.currentProperties = currentProperties;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/editor/{widgetName}")
    public String editor(@PathVariable("widgetName") String widgetName, Model model) {
        model.addAttribute("widgetId", widgetName);
        return "editor";
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/browse/{widgetName}/{styleId}"})
    public String browse(@PathVariable("widgetName") String widgetName
            ,@PathVariable("styleId") String styleId,Model model) {

        InstalledWidget widget = widgetLocateService.findWidget(widgetName);
        Component component = new Component();
        component.setProperties(currentProperties);
        component.setStyleId(styleId);
        component.setWidget(widget);

        model.addAttribute("component", component);
        return "browse";
    }


}
