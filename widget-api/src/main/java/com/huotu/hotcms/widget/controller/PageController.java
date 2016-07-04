/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}"})
    public void page(@PathVariable("pagePath") String pagePath
            , HttpServletRequest request, HttpServletResponse response) {
        try {
            CMSContext cmsContext = CMSContext.RequestContext();
            //查找当前站点下指定pagePath的page
            Page page = pageService.findByPagePath(cmsContext.getSite(), pagePath);
            //生成page htmlCode
            String html = "<div>";
            for (PageElement pageElement : page.getElements()) {
                html = widgetResolveService.pageElementHTML(pageElement, cmsContext);
            }
            html += "</div>";

            //查找页面数据源
            Category category = page.getCategory();

            //查找当前站点下指定数据源的page，且page类型为数据内容
            AbstractContent content = null;

        } catch (IOException e) {

        }


    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}/{contentId}"})
    public void page(@PathVariable("pagePath") String pagePath, @PathVariable("contentId") Long contentId
            , HttpServletRequest request, HttpServletResponse response) {
        try {
            CMSContext cmsContext = CMSContext.RequestContext();
            Page page = pageService.findByPagePath(cmsContext.getSite(), pagePath);
            Category category = page.getCategory();
            String html = "<div>";
            for (PageElement pageElement : page.getElements()) {
                html = widgetResolveService.pageElementHTML(pageElement, cmsContext);
            }
            html += "</div>";

            AbstractContent content = abstractContentRepository.findOne(contentId);
            if (content != null && Objects.equals(content.getCategory().getId(), category.getId())) {

            }

        } catch (IOException e) {
        }

//        Category category = null;// from pagePath
//        assert (category != null);
//        AbstractContent content = abstractContentRepository.findOne(contentId);// from Site category contentId

//        404 if content is not existing or access defined.

//        Page page = null;//

    }

}
