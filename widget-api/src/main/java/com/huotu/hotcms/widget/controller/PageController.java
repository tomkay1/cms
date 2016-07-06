/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.service.PageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户获取page页面html Code 页面服务相关
 * Created by lhx on 2016/7/2.
 */
@Controller
@RequestMapping(value = "/_web")
public class PageController implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(PageController.class);

    @Autowired
    private AbstractContentRepository abstractContentRepository;

    @Autowired
    private PageService pageService;

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}"})
    public void pageIndex(@PathVariable("pagePath") String pagePath, HttpServletResponse response) {
        CMSContext cmsContext = CMSContext.RequestContext();
        //查找当前站点下指定pagePath的page
        try {
            Page page = pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
            if (page != null) {
                pageService.generateHTML(response.getOutputStream(), page, cmsContext);
                response.setContentType("text/html;charset=utf-8");
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}/{contentId}"})
    public void pageContent(@PathVariable("pagePath") String pagePath, @PathVariable("contentId") Long contentId
            , HttpServletResponse response) {
        try {
            CMSContext cmsContext = CMSContext.RequestContext();
            //查找数据内容
            AbstractContent content = abstractContentRepository.findOne(contentId);
            if (content != null) {
                cmsContext.setAbstractContent(content);
                //查找当前站点下指定数据源pagePath下最接近的page
                Page page = pageService.getClosestContentPage(content.getCategory(), pagePath);

                pageService.generateHTML(response.getOutputStream(), page, cmsContext);

                response.setContentType("text/html;charset=utf-8");
                return;
            }//404 content is not existing or access defined.
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
    }


    @Override
    public FilterStatus doSiteFilter(Site site, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String targetPath = "/_web" + request.getRequestURI();
            log.debug("Forward to " + targetPath);
            request.getRequestDispatcher(targetPath)
                    .forward(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }
        return FilterStatus.STOP;
    }

    @Override
    public int getOrder() {
        // 最低优先级 到了我这  就是改url了
        return Ordered.LOWEST_PRECEDENCE;
    }
}
