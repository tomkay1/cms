/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageModel;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * <b>页面管理服务</b>
 * <em>响应码说明：</em>
 * <ul>
 * <li>202-成功接收客户端发来的请求</li>
 * <li>502-出现异常，具体待定</li>
 * </ul>
 * <p>07.22 将交互的主体更换为{@link PageModel},系统内部只认准{@link PageInfo}</p>
 *
 * @author wenqi
 * @since v2.0
 */
@Controller
public class PageController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private WidgetResolveService widgetResolveService;

    @Autowired
    private WidgetLocateService widgetLocateService;

    @Autowired
    private ResourceService resourceService;

    /**
     * <p>获取页面{@link PageInfo}</p>
     *
     * @param siteId 站点ID
     * @return 拿到相应的界面信息列表
     * @throws IOException 异常
     * @see PageInfo
     */
    @RequestMapping(value = "/manage/{siteId}/pages", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PageInfo> getPageList(@PathVariable("siteId") Long siteId) throws IOException {
        Site site = siteRepository.findOne(siteId);
        if (site == null)
            throw new IllegalStateException("读取到的站点为空");
        return pageService.getPageList(site);
    }


    /**
     * 获取某一具体页面
     *
     * @param pageId 页面ID
     * @return 页面信息
     * @throws IOException 其他异常
     */
    @RequestMapping(value = "/manage/pages/{pageId}", method = RequestMethod.GET
            , produces = "application/json; charset=UTF-8")
    @ResponseBody
    public PageModel getPage(@PathVariable("pageId") Long pageId) throws IOException, PageNotFoundException {
        PageInfo pageInfo = pageService.getPage(pageId);
        PageModel pageModel = new PageModel();
        pageModel.setPageIdentity(pageInfo.getId());
        pageModel.setTitle(pageInfo.getTitle());
        if (pageInfo.getLayout() != null) {
            // 要给它设置previewHTML
            for (Layout layout : pageInfo.getLayout().getRoot()) {
                toModel(layout);
            }
            pageModel.setRoot(pageInfo.getLayout().getRoot());
            pageModel.setStyleSheet(pageInfo.getLayout().getStyleSheet());
        }
        return pageModel;
    }

    private void toModel(PageElement pageElement) {
        // 要给它设置previewHTML
        if (pageElement instanceof Layout) {
            Layout layout = (Layout) pageElement;
            for (PageElement e : layout.elements()) {
                toModel(e);
            }
        } else if (pageElement instanceof Component) {
            Component component = (Component) pageElement;
            InstalledWidget installWidget = widgetLocateService.findWidget(component.getWidgetIdentity());
            String previewHTML = widgetResolveService.previewHTML(installWidget.getWidget(), component.getStyleId()
                    , CMSContext.RequestContext(), component.getProperties());
            component.setPreviewHTML(previewHTML);
        }
    }

    /**
     * <p>保存界面{@link PageModel}信息到pageId相关的{@link PageInfo}中</p>
     *
     * @throws IOException 从request中读取请求体时异常
     */
    @RequestMapping(value = "/manage/pages/{pageId}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void savePage(HttpServletRequest request, @PathVariable("pageId") Long pageID, @RequestBody String pageJson)
            throws IOException, URISyntaxException, PageNotFoundException {
//        String pageJson = CharStreams.toString(request.getReader());
        PageModel page = objectMapper.readValue(pageJson, PageModel.class);
//        page.setPageIdentity(pageID);
        pageService.savePage(pageService.getPage(pageID), page, false);
    }

    /**
     * <p>删除页面</p>
     *
     * @param pageId 页面ID
     */
    @RequestMapping(value = "/manage/pages/{pageId}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deletePage(@PathVariable("pageId") Long pageId) throws IOException {
        pageService.deletePage(pageId);
    }

    /**
     * 保存页面部分属性
     *
     * @param pageId       页面ID
     * @param propertyName 要保存的属性名
     */
    @RequestMapping(value = "/manage/pages/{pageId}/{propertyName}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void savePagePartProperties(@PathVariable("pageId") Long pageId
            , @PathVariable("propertyName") String propertyName) throws IOException, PageNotFoundException {
        throw new NoSuchMethodError("not support yet");
    }


    /**
     * 跳转到CMS编辑界面
     *
     * @return url
     */
    @RequestMapping("/manage/page/edit/{pageId}")
    public String startEdit(@PathVariable("pageId") long pageId, Model model) throws PageNotFoundException {
        PageInfo pageInfo = pageService.getPage(pageId);
        CMSContext.RequestContext().updateSite(pageInfo.getSite());

        if (pageInfo.getLayout() != null) {
            try {
                //删除控件旧的css样式表
                if (pageInfo.getResourceKey() != null) {
                    resourceService.deleteResource(pageInfo.getPageCssResourcePath());
                }
                String resourceKey = UUID.randomUUID().toString();
                pageInfo.setResourceKey(resourceKey);
                //生成page的css样式表
                Layout[] elements = pageService.layoutsForUse(pageInfo.getLayout());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                for (Layout element : elements) {
                    //生成组件css
                    widgetResolveService.widgetDependencyContent(CMSContext.RequestContext(), null, Widget.CSS, element, buffer);
                }
                Resource resource = resourceService.uploadResource(pageInfo.getPageCssResourcePath()
                        , new ByteArrayInputStream(buffer.toByteArray()));
                model.addAttribute("pageCss", resource.httpUrl());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageId", pageId);
        return "/edit/edit.html";
    }
}
