/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 查询一个控件相关信息的
 *
 * @author CJ
 */
@Controller
@RequestMapping("/widget")
public class WidgetController {

    @Autowired(required = false)
    private WidgetLocateService widgetLocateService;
    @Autowired
    private WidgetResolveService widgetResolveService;

    @RequestMapping("/previewHtml")
    public ResponseEntity previewHtml(String identifier, String styleId, ComponentProperties properties)
            throws PageNotFoundException, IOException {
        InstalledWidget installedWidget = widgetLocateService.findWidget(identifier);
        if (installedWidget == null) {
            return ResponseEntity.notFound().build();
        }
        String html = widgetResolveService.previewHTML(installedWidget.getWidget(), styleId, CMSContext.RequestContext()
                , properties);
        return ResponseEntity.ok().contentType(Widget.HTML).body(html);
    }

    @RequestMapping("/preview/{pageId}/{componentId}.css")
    public ResponseEntity componentCss(@PathVariable String pageId, @PathVariable String componentId)
            throws PageNotFoundException, IOException {

        return null;
    }

    @RequestMapping("/")
    public ResponseEntity previewCss(@PathVariable WidgetIdentifier identifier) throws PageNotFoundException, IOException {
        // StandardLinkBuilder
        return content(identifier, Widget.Javascript);
    }


    @RequestMapping("/{identifier}.js")
    public ResponseEntity javascript(@PathVariable WidgetIdentifier identifier) throws PageNotFoundException, IOException {
        // StandardLinkBuilder
        return content(identifier, Widget.Javascript);
    }

    @RequestMapping("/{identifier}.css")
    public ResponseEntity css(@PathVariable WidgetIdentifier identifier) throws PageNotFoundException, IOException {
        return content(identifier, Widget.CSS);
    }

    private ResponseEntity content(@PathVariable WidgetIdentifier identifier, MediaType type) throws IOException {
        InstalledWidget widget = widgetLocateService.findWidget(identifier.getGroupId(), identifier.getArtifactId()
                , identifier.getVersion());

        if (widget == null) {
//            throw new PageNotFoundException();
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        widgetResolveService.widgetDependencyContent(CMSContext.RequestContext(), widget.getWidget(), type, null, buffer);
        return ResponseEntity.ok().contentType(type).body(buffer.toString("UTF-8"));
    }


}
