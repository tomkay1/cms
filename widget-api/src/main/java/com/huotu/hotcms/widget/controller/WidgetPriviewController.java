package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lhx on 2016/7/27.
 */
@Controller
@RequestMapping(value = "/priview")
public class WidgetPriviewController {

    @Autowired(required = false)
    private WidgetResolveService widgetResolveService;

    @Autowired(required = false)
    private WidgetLocateService widgetLocateService;

    /**
     * 获取指定控件,指定样式,的控件预览视图htmlCode
     *
     * 成功：状态200，并返回控件 priviewHtml Code
     * 失败：状态403 无htmlCode
     * @param widgetIdentifier {@link com.huotu.hotcms.service.entity.support.WidgetIdentifier}
     * @param styleId          样式id
     * @param properties       控件参数
     */
    @RequestMapping(value = "/priviewHtml", method = RequestMethod.POST)
    public void privewHtml(@RequestParam(required = false) String widgetIdentifier
            , @RequestParam(required = false) String styleId
            , @RequestParam(required = false) ComponentProperties properties, HttpServletResponse response) {
            response.setContentType("text/html;charset=utf-8");
            try{
                InstalledWidget installedWidget = widgetLocateService.findWidget(widgetIdentifier);
                installedWidget.getWidget().valid(styleId,properties);
                String priviewHtml = widgetResolveService.previewHTML(installedWidget.getWidget(), styleId
                        , CMSContext.RequestContext(), properties);
                response.getOutputStream().write(priviewHtml.getBytes(),0,priviewHtml.getBytes().length);
                response.setStatus(200);
            }catch (Exception e){
                response.setStatus(403);
            }
    }
}
