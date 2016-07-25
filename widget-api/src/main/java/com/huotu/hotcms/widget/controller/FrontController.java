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
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.AbstractContentRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 用户获取page页面html Code 页面服务相关
 * Created by lhx on 2016/7/2.
 */
@Controller
@RequestMapping(value = "/_web")
public class FrontController implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(FrontController.class);
    private static final String htmlFooter = "\n</body>\n" +
            "    <script src=\"http://resali.huobanplus.com/cdn/jquery/1.9.1/jquery.min.js\"></script>\n" +
            "    <script src=\"http://resali.huobanplus.com/cdn/bootstrap/3.3.6/bootstrap.min.js\"></script>\n" +
            "    </html>";
    private static String htmlHeader = "<!DOCTYPE html>\n" +
            "    <html lang=\"en\" >\n" +
            "    <head>\n" +
            "    <meta charset=\"utf-8\"/>\n" +
            "    <meta http-equiv=\"x-ua-compatible\" content=\"IE=edge,chrome=1\">\n" +
            "    <meta name=\"author\" content=\"Neo\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no\">\n" +
            "    <meta name=\"keywords\" content=\"%s\">\n" +
            "    <meta name=\"description\" content=\"%s\">\n" +
            "    <title>%s</title>\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"http://resali.huobanplus.com/cdn/bootstrap/3.3.6/css/bootstrap.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/index.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"%s\">\n" +
            "\n" +
            "    </head>\n" +
            "    <body>\n";
    @Autowired(required = false)
    private AbstractContentRepository abstractContentRepository;
    @Autowired(required = false)
    private PageService pageService;
    @Autowired(required = false)
    private PageInfoRepository pageInfoRepository;
    @Autowired(required = false)
    private ResourceService resourceService;

    /**
     * 用于支持首页的浏览
     *
     * @param response
     * @param model
     */
    @RequestMapping(method = RequestMethod.GET, value = {"", "/"})
    public void pageIndex(HttpServletResponse response, Model model) throws IOException, PageNotFoundException
            , URISyntaxException {
        pageIndex("", response, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}"})
    public void pageIndex(@PathVariable("pagePath") String pagePath, HttpServletResponse response, Model model)
            throws PageNotFoundException, IOException, URISyntaxException {
        CMSContext cmsContext = CMSContext.RequestContext();
        //查找当前站点下指定pagePath的page
        PageInfo page = pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
        if (page != null) {
            generateHtml(response, page, cmsContext, model);
            return;
        }
        response.setStatus(HttpStatus.SC_NOT_FOUND);

    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}/{contentId}"})
    public void pageContent(@PathVariable("pagePath") String pagePath, @PathVariable("contentId") Long contentId
            , HttpServletResponse response, Model model) throws IOException, PageNotFoundException, URISyntaxException {
        CMSContext cmsContext = CMSContext.RequestContext();
        //查找数据内容
        AbstractContent content = abstractContentRepository.findOne(contentId);
        if (content != null) {
            cmsContext.setAbstractContent(content);
            //查找当前站点下指定数据源pagePath下最接近的page
            PageInfo page = pageService.getClosestContentPage(content.getCategory(), pagePath);
            generateHtml(response, page, cmsContext, model);
            return;
        } else {
            PageInfo page = pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
            if (page != null) {
                generateHtml(response, page, cmsContext, model);
                return;
            }
        }//404 content is not existing or access defined.
        response.setStatus(HttpStatus.SC_NOT_FOUND);
    }


    private void generateHtml(HttpServletResponse response, PageInfo pageInfo, CMSContext cmsContext, Model model)
            throws IOException {
        model.addAttribute("resourceKey", pageInfo.getResourceKey());
        model.addAttribute("pageId", pageInfo.getPageId());
    private void generateHtml(HttpServletResponse response, Page page, CMSContext cmsContext, Model model)
            throws IOException, URISyntaxException {
        PageInfo pageInfo = pageInfoRepository.findOne(page.getPageIdentity());

        String path = "page/resource/css/" + pageInfo.getResourceKey() + "/" + pageInfo.getPageId() + ".css";
        Resource resource = resourceService.getResource(path);
        htmlHeader = String.format(htmlHeader, pageInfo.getSite().getKeywords(), pageInfo.getSite().getDescription()
                , pageInfo.getTitle());
        response.getOutputStream().write(htmlHeader.getBytes(), 0, htmlHeader.getBytes().length);
        pageService.generateHTML(response.getOutputStream(), pageInfo, cmsContext);
        response.getOutputStream().write(htmlFooter.getBytes(), 0, htmlFooter.getBytes().length);
                , pageInfo.getTitle(), resource.httpUrl().toURI().toString());
        String htmlContent = pageService.generateHTML(page, cmsContext);
        String pagehtml = htmlHeader + htmlContent + htmlFooter;
        response.getOutputStream().write(pagehtml.getBytes(), 0, pagehtml.getBytes().length);
        response.setContentType("text/html;charset=utf-8");
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
