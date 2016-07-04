/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Created by lhx on 2016/7/2.
 */
@Controller
@RequestMapping(value = "/_web")
public class PageController {
    @Autowired
    private AbstractContentRepository abstractContentRepository;

    @Autowired
    private PageService pageService;


    @Autowired
    private WidgetResolveService widgetResolveService;

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}"})
    public void pageIndex(@PathVariable("pagePath") String pagePath, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        CMSContext cmsContext = CMSContext.RequestContext();
        //查找当前站点下指定pagePath的page
        Page page = pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
        if (page != null) {
            //生成page htmlCode
            String html = "<div>";
            for (PageElement pageElement : page.getElements()) {
                html += widgetResolveService.pageElementHTML(pageElement, cmsContext);
            }
            html += "</div>";
            out.write(html);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}/{contentId}"})
    public void pageContent(@PathVariable("pagePath") String pagePath, @PathVariable("contentId") Long contentId
            , HttpServletResponse response) throws IllegalStateException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        CMSContext cmsContext = CMSContext.RequestContext();
        Site site = cmsContext.getSite();
        //查找数据内容
        AbstractContent content = abstractContentRepository.findOne(contentId);
        if (content != null) {
            cmsContext.setAbstractContent(content);

            //查找当前站点下指定pagePath的page
            com.huotu.hotcms.service.entity.Page pageInfo = pageService.findBySiteAndPagePath(site.getSiteId()
                    , pagePath);

            if (pageInfo != null) {
                //判断数据源
                if (content.getCategory().getId().equals(pageInfo.getCategory().getId())) {
                    Page page = pageService.findByCategoryAndContent(pageInfo.getCategory(), content);
                    if (page != null) {
                        PageElement[] elements = page.getElements();
                        //生成page htmlCode
                        String html = "<div>";
                        for (int i = 0, l = elements.length; i < l; i++) {
                            html += widgetResolveService.pageElementHTML(elements[i], cmsContext);
                        }
                        html += "</div>";
                        out.write(html);
                    }
                }
            }
        }//404 content is not existing or access defined.



    }

}
