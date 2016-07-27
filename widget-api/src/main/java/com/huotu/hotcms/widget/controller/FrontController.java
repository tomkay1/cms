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
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.service.PageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 用户获取page页面html Code 页面服务相关
 * <p>新增preview相关</p>
 * Created by lhx on 2016/7/2.
 */
@Controller
public class FrontController implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(FrontController.class);

    @Autowired(required = false)
    private ContentRepository contentRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private WidgetResolveService widgetResolveService;

    /**
     * 参考<a href="https://huobanplus.quip.com/Y9mVAeo9KnTh">可用的CSS 资源</a>
     *
     * @param pageId 页面id
     * @param id     组件id
     * @return css内容
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/preview/{pageId}/{id}.css"}, produces = "text/css")
    public ResponseEntity previewCss(@PathVariable("pageId") long pageId, @PathVariable("id") String id) throws IOException {
        try {
            PageInfo pageInfo = pageService.getPage(pageId);

            // 寻找控件了
            for (Layout layout : PageLayout.NoNullLayout(pageInfo.getLayout())) {
                Component component = findComponent(layout, id);
                if (component != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    widgetResolveService.componentCSS(CMSContext.RequestContext(), component, buffer);
                    return ResponseEntity
                            .ok()
                            .contentType(MediaType.parseMediaType("text/css"))
                            .body(buffer.toByteArray());
                }
            }

            return ResponseEntity.notFound().build();

        } catch (PageNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Component findComponent(PageElement element, String id) {
        if (element instanceof Component) {
            if (id.equals(((Component) element).getId())) {
                return (Component) element;
            }
            return null;
        }
        if (element instanceof Layout) {
            for (PageElement element1 : ((Layout) element).elements()) {
                Component component = findComponent(element1, id);
                if (component != null)
                    return component;
            }
        }
        return null;
    }

    /**
     * 用于支持首页的浏览
     *
     * @param model
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/_web", "/_web/"})
    public PageInfo pageIndex(Model model) throws IOException, PageNotFoundException {
        return pageIndex("", model);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/_web/{pagePath}"})
    public PageInfo pageIndex(@PathVariable("pagePath") String pagePath, Model model)
            throws PageNotFoundException, IOException {
        CMSContext cmsContext = CMSContext.RequestContext();
        model.addAttribute("time", System.currentTimeMillis());
        //查找当前站点下指定pagePath的page
        return pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/_web/{pagePath}/{contentId}"})
    public PageInfo pageContent(@PathVariable("pagePath") String pagePath, @PathVariable("contentId") Long contentId
            , Model model) throws IOException, PageNotFoundException {
        CMSContext cmsContext = CMSContext.RequestContext();
        model.addAttribute("time", System.currentTimeMillis());
        //查找数据内容
        AbstractContent content = contentRepository.findOne(contentId);
        if (content != null) {
            cmsContext.setAbstractContent(content);
            //查找当前站点下指定数据源pagePath下最接近的page
            return pageService.getClosestContentPage(content.getCategory(), pagePath);
        } else {
            return pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);
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
