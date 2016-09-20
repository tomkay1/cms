/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.Template;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.service.ContentService;
import com.huotu.hotcms.service.service.TemplateService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageModel;
import com.huotu.hotcms.widget.resolve.WidgetILinkBuilder;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 用户获取page页面html Code 页面服务相关
 * <p>新增preview相关</p>
 * Created by lhx on 2016/7/2.
 */
@Controller
public class FrontController implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(FrontController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    WidgetLocateService widgetLocateService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private PageService pageService;
    @Autowired
    private WidgetResolveService widgetResolveService;
    @Autowired
    private TemplateService templateService;

    @Autowired
    private ResourceService resourceService;

    /**
     * 参考<a href="https://huobanplus.quip.com/Y9mVAeo9KnTh">可用的CSS 资源</a>
     *
     * @param pageId 页面id
     * @param id     组件id
     * @return css内容
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/previewCss/{pageId}/{componentId}.css"}
            , produces = "text/css")
    public ResponseEntity previewCss(@PathVariable("pageId") long pageId, @PathVariable("componentId") String id)
            throws IOException {
        try {
            PageInfo pageInfo = pageService.getPage(pageId);
            // 寻找控件了
            for (Layout layout : pageService.layoutsForUse(pageInfo.getLayout())) {
                Component component = findComponent(layout, id);
                if (component != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    widgetResolveService.widgetDependencyContent(CMSContext.RequestContext()
                            , component.getInstalledWidget().getWidget(), Widget.CSS, layout, buffer);
                    return ResponseEntity
                            .ok()
                            .contentType(Widget.CSS)
                            .body(buffer.toByteArray());
                }
            }
            return ResponseEntity.notFound().build();
        } catch (PageNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 获取指定控件,指定样式,的控件预览视图htmlCode
     * <p>
     * 成功：状态200，并返回控件 previewHtml Code
     * 失败：状态404 无htmlCode
     * <p>
     * widgetIdentifier {@link com.huotu.hotcms.service.entity.support.WidgetIdentifier}
     * styleId          样式id
     * properties       控件参数
     *
     * @throws IOException 资源未找到
     */
    @RequestMapping(value = "/preview/component", method = RequestMethod.POST)
    public ResponseEntity previewHtml(@RequestBody String json) throws IOException {
        return getPreviewComponentResponseEntity(json);
    }

    private ResponseEntity getPreviewComponentResponseEntity(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        String widgetIdentifier = (String) map.get("widgetIdentity");
        if (widgetIdentifier == null) {
            return ResponseEntity.notFound().build();
        }
        String styleId = styleIdFromMap(map);
//        String id = (String) map.get("id");
        String componentId = (String) map.get("componentId");
        Map properties = (Map) map.get("properties");
        ComponentProperties componentProperties = new ComponentProperties();
        if (properties != null)
            //noinspection unchecked
            componentProperties.putAll(properties);
        try {
            InstalledWidget installedWidget = widgetLocateService.findWidget(widgetIdentifier);

            {
                //补丁
                styleId = WidgetStyle.styleByID(installedWidget.getWidget(), styleId).id();
            }

            installedWidget.getWidget().valid(styleId, componentProperties);
            String previewHTML = widgetResolveService.previewHTML(installedWidget.getWidget(), styleId
                    , CMSContext.RequestContext(), componentProperties);

            // 生成好的css
            Resource resource = null;
            String resourcePath = null;
            // 决定是否生成css
            org.springframework.core.io.Resource cssResource = installedWidget.getWidget()
                    .widgetDependencyContent(Widget.CSS);

            if (cssResource != null && cssResource.exists()) {
                Path path = Files.createTempFile("tempCss", ".css");
                try {

                    try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.WRITE)) {
                        Component component = new Component();
                        component.setId(componentId);
                        component.setInstalledWidget(installedWidget);
                        component.setProperties(componentProperties);
                        component.setWidgetIdentity(widgetIdentifier);
                        component.setStyleId(styleId);

                        widgetResolveService.widgetDependencyContent(CMSContext.RequestContext()
                                , installedWidget.getWidget(), Widget.CSS, component, out);
                        out.flush();
                    }

                    try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                        resourcePath = "tmp/page/" + UUID.randomUUID().toString() + ".css";
                        resource = resourceService.uploadResource(resourcePath, is);
                    }

                } finally {
                    //noinspection ThrowFromFinallyBlock
                    Files.deleteIfExists(path);
                }
            }

            return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=utf-8"))
                    .header("cssLocation", resource != null ? resource.httpUrl().toString() : "")
                    .header("cssPath", resource != null ? resourcePath : "")
                    .body(previewHTML.getBytes("utf-8"));
        } catch (Exception e) {
            log.warn("Unknown Exception", e);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON_UTF8).body(e.getLocalizedMessage());
        }
    }

    /**
     * 获取指定控件,指定样式,的控件预览视图htmlCode
     * <p>
     * 成功：状态200，并返回控件 previewHtml Code
     * 失败：状态404 无htmlCode
     * <p>
     * widgetIdentifier {@link com.huotu.hotcms.service.entity.support.WidgetIdentifier}
     * styleId          样式id
     * id           页面id
     * properties       控件参数
     */
    @RequestMapping(value = "/preview/widgetEditor", method = RequestMethod.POST)
    public ResponseEntity widgetEditor(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        String styleId = styleIdFromMap(map);
        String widgetIdentifier = (String) map.get("widgetIdentity");
        Map properties = (Map) map.get("properties");
        if (widgetIdentifier == null || properties == null) {
            return ResponseEntity.notFound().build();
        }
        ComponentProperties componentProperties = new ComponentProperties();
        //noinspection unchecked
        componentProperties.putAll(properties);
        InstalledWidget installedWidget = widgetLocateService.findWidget(widgetIdentifier);
        installedWidget.getWidget().valid(styleId, componentProperties);
        String htmlCode = widgetResolveService.editorHTML(installedWidget.getWidget(), CMSContext.RequestContext()
                , componentProperties);
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=utf-8"))
                .body(htmlCode.getBytes("utf-8"));
    }

    @Nullable
    private String styleIdFromMap(Map map) {
        String styleId;
        if (map.containsKey("styleId")) {
            Object o = map.get("styleId");
            if (o != null)
                styleId = o.toString();
            else
                styleId = null;
        } else
            styleId = null;
        return styleId;
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

    @RequestMapping(method = RequestMethod.POST, value = "/pagePreview/{pageId}")
    @Transactional(readOnly = true)
    public PageInfo pagePreview(@AuthenticationPrincipal Login login, Model model, String jsonString
            , @PathVariable("pageId") long pageId) throws PageNotFoundException, IOException {
        PageInfo pageInfo = pageService.getPage(pageId);

        PageModel page = objectMapper.readValue(jsonString, PageModel.class);

        if (login.isRoot())
            pageInfo.setResourceKey("PREVIEW");
        else
            pageInfo.setResourceKey("PREVIEW" + login.currentOwnerId());

        CMSContext cmsContext = CMSContext.RequestContext();
        cmsContext.updateSite(pageInfo.getSite());

        return returnPage(model, cmsContext, pageInfo);
    }

    /**
     * 用于支持首页的浏览
     *
     * @param model 模型
     * @throws PageNotFoundException 页面没找到或
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/_web", "/_web/"})
    public PageInfo pageIndex(Model model) throws PageNotFoundException {
        return pageIndex("", model);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/_web/{pagePath}"})
    public PageInfo pageIndex(@PathVariable("pagePath") String pagePath, Model model)
            throws PageNotFoundException {
        CMSContext cmsContext = CMSContext.RequestContext();
        if (cmsContext.getSite() instanceof Template && pagePath.isEmpty()) {
            templateService.preview((Template) cmsContext.getSite());
        }
        HttpServletRequest request = cmsContext.getRequest();
        if (request.getParameterMap().size() > 0) {
            //处理页面参数
            Map<String, String[]> map = request.getParameterMap();
            Set<Map.Entry<String, String[]>> set = map.entrySet();
            Map<String, Map<String, String>> parameters = map.isEmpty() ? null : new HashMap<>();
            for (Map.Entry<String, String[]> entry : set) {
                String id_key[] = entry.getKey().split(WidgetILinkBuilder.A);
                if (id_key.length != 2)
                    continue;
                if (parameters.containsKey(id_key[0])) {
                    parameters.get(id_key[0]).put(id_key[1], request.getParameter(entry.getKey()));
                } else {
                    parameters.put(id_key[0], new HashMap<>());
                    parameters.get(id_key[0]).put(id_key[1], request.getParameter(entry.getKey()));
                }
            }
            cmsContext.setParameters(parameters);
        }
        //查找当前站点下指定pagePath的page
        PageInfo pageInfo = pageService.findBySiteAndPagePath(cmsContext.getSite(), pagePath);

        return returnPage(model, cmsContext, pageInfo);
    }

    private PageInfo returnPage(Model model, CMSContext cmsContext, PageInfo pageInfo) {
        model.addAttribute("time", System.currentTimeMillis());
        cmsContext.setCurrentPage(pageInfo);
        return pageInfo;
    }

    /**
     * 页面内容
     *
     * @param pagePath      pagePath
     * @param contentSerial contentSerial
     * @param model         model
     * @return
     * @throws IOException           未找到数据
     * @throws PageNotFoundException 页面没找到
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/_web/{pagePath}/{contentSerial}"})
    public PageInfo pageContent(@PathVariable("pagePath") String pagePath, @PathVariable("contentSerial") String contentSerial
            , Model model) throws IOException, PageNotFoundException {
        CMSContext cmsContext = CMSContext.RequestContext();
        model.addAttribute("time", System.currentTimeMillis());
        //查找数据内容
        AbstractContent content = contentService.getContent(cmsContext.getSite(), contentSerial);
        model.addAttribute("abstractContent", content);
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
