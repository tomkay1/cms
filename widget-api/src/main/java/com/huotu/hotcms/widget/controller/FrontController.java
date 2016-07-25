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
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用户获取page页面html Code 页面服务相关
 * Created by lhx on 2016/7/2.
 */
@Controller
@RequestMapping(value = "/_web")
public class FrontController implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(FrontController.class);

    private final Template htmlTemplate;
    @Autowired(required = false)
    private AbstractContentRepository abstractContentRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private ResourceService resourceService;

    public FrontController() throws IOException {
        try (InputStream propertiesFile = new ClassPathResource("/front/velocity.properties").getInputStream()) {
            Properties properties = new Properties();
            properties.load(propertiesFile);
            Velocity.init(properties);
        }

        htmlTemplate = Velocity.getTemplate("/front/html.vm");
    }

    /**
     * 用于支持首页的浏览
     *
     * @param response
     * @param model
     */
    @RequestMapping(method = RequestMethod.GET, value = {"", "/"})
    public void pageIndex(HttpServletResponse response, Model model) throws IOException, PageNotFoundException {
        pageIndex("", response, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/{pagePath}"})
    public void pageIndex(@PathVariable("pagePath") String pagePath, HttpServletResponse response, Model model)
            throws PageNotFoundException, IOException {
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
            , HttpServletResponse response, Model model) throws IOException, PageNotFoundException {
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

        String content = pageService.generateHTML(pageInfo, cmsContext);

        VelocityContext context = new VelocityContext();

        context.put("keywords", pageInfo.getSite().getKeywords());
        context.put("description", pageInfo.getSite().getDescription());
        context.put("title", pageInfo.getTitle());
        context.put("globalCssURI", "/css/index.css");
        context.put("pageCssURI", resourceService.getResource(pageInfo.getPageCssResourcePath()).httpUrl());
        context.put("content", content);

        response.setContentType("text/html;charset=utf-8");

        htmlTemplate.merge(context, response.getWriter());
        response.getWriter().flush();

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
