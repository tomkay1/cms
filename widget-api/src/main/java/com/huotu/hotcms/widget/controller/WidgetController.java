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
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查询一个控件相关信息的
 *
 * @author CJ
 */
@Controller
@RequestMapping("/widget")
public class WidgetController {

    @Autowired
    private WidgetLocateService widgetLocateService;
    @Autowired
    private WidgetResolveService widgetResolveService;

    @RequestMapping("/{identifier}.js")
    public ResponseEntity widgetJs(@PathVariable WidgetIdentifier identifier) throws PageNotFoundException {
        // StandardLinkBuilder
        InstalledWidget widget = widgetLocateService.findWidget(identifier.getGroupId(), identifier.getArtifactId()
                , identifier.getVersion());

        if (widget == null) {
//            throw new PageNotFoundException();
            return ResponseEntity.notFound().build();
        }

        String code = widgetResolveService.widgetJavascript(CMSContext.RequestContext(), widget.getWidget());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/javascript")).body(code);
    }

}
