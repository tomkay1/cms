/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.bean;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.event.CopySiteEvent;
import com.huotu.hotcms.service.event.DeleteSiteEvent;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CommonService;
import com.huotu.hotcms.service.service.TemplateService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private WidgetResolveService widgetResolveService;
    @Autowired
    private WidgetLocateService widgetLocateService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public void siteDeleted(DeleteSiteEvent event) throws IOException {
        for (PageInfo pageInfo : pageInfoRepository.findBySite(event.getSite())) {
            deletePage(pageInfo.getId());
        }
    }

    @Override
    public void siteCopy(CopySiteEvent event) throws IOException {
        for (PageInfo src : pageInfoRepository.findBySite(event.getSrc())) {
            PageInfo newOne = src.copy();

            String append = "";
            while (pageInfoRepository.findBySiteAndPagePath(event.getDist(), newOne.getPagePath() + append) != null) {
                append = append + TemplateService.DuplicateAppend;
            }

            newOne.setPagePath(newOne.getPagePath() + append);
            newOne.setSite(event.getDist());
//            newOne.setParent(null);
            if (newOne.getCategory() != null) {
                newOne.setCategory(categoryRepository.findBySerialAndSite(newOne.getCategory().getSerial()
                        , event.getDist()));
            }

            savePage(newOne, null, false);
        }
    }

    @Override
    public Layout[] layoutsForUse(PageLayout page) {
        Layout[] layouts = PageLayout.NoNullLayout(page);
        //
        for (Layout layout : layouts) {
            for (PageElement element : layout.elements()) {
                if (element instanceof Component) {
                    Component component = (Component) element;
                    if (component.getInstalledWidget() == null) {
                        component.setInstalledWidget(widgetLocateService.findWidget(component.getWidgetIdentity()));
                    }
                }
            }
        }
        return layouts;
    }

    @Override
    public String generateHTML(PageInfo page, CMSContext context) {
        StringWriter writer = new StringWriter();
        try {
            generateHTML(writer, page, context);
        } catch (IOException e) {
            throw new IllegalStateException("Mem IO", e);
        }
        return writer.toString();
    }

    @Override
    public void generateHTML(Writer writer, PageInfo page, CMSContext context) throws IOException {
        Layout[] layouts = layoutsForUse(page.getLayout());
        writer.append("<div>");
        for (PageElement element : layouts) {
            writer.append("<div class=\"row\">");
            widgetResolveService.pageElementHTML(element, context, writer);
            writer.append("</div>");
        }
        writer.append("</div>");
    }


    @Override
    public void savePage(PageInfo info, PageModel model, boolean preview) throws IOException {
        if (info.getSerial() == null) {
            info.setSerial(UUID.randomUUID().toString().replace("-", ""));
        }
        info.setUpdateTime(LocalDateTime.now());
        //删除控件旧的css样式表
        if (info.getResourceKey() != null) {
            resourceService.deleteResource(info.getPageCssResourcePath());
        }
        //保存最新控件信息
        if (!preview) {
            String resourceKey = UUID.randomUUID().toString();
            info.setResourceKey(resourceKey);
        }


        if (model != null)//如果没有传model过来 不应该改变布局
            info.setLayout(PageLayout.FromWeb(PageLayout.NoNullLayout(model)));

        if (!preview)
            info = pageInfoRepository.save(info);
        //生成page的css样式表
        Layout[] elements = layoutsForUse(info.getLayout());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (Layout element : elements) {
            //生成组件css
            widgetResolveService.widgetDependencyContent(CMSContext.RequestContext(), null, Widget.CSS, element, buffer);
        }

        resourceService.uploadResource(info.getPageCssResourcePath(), new ByteArrayInputStream(buffer.toByteArray()));
    }

    @Override
    public PageInfo getPage(Long pageId) throws PageNotFoundException {
        PageInfo pageInfo = pageInfoRepository.findOne(pageId);
        if (pageInfo == null)
            throw new PageNotFoundException("页面ID为" + pageId + "的页面不存在");
        return pageInfo;
    }

    @Override
    public void deletePage(Long pageId) throws IOException {
        commonService.deleteResource(pageInfoRepository.getOne(pageId));
        pageInfoRepository.delete(pageId);
    }

    @Override
    public PageInfo findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException
            , PageNotFoundException {
        PageInfo pageInfo = pageInfoRepository.findBySiteAndPagePath(site, pagePath);
        if (pageInfo == null)
            throw new PageNotFoundException();
        return pageInfo;
    }

    @Override
    public List<PageInfo> getPageList(Site site) {
        return pageInfoRepository.findBySite(site);
    }

    @Override
    public PageInfo getClosestContentPage(Category category, String path) throws PageNotFoundException {
        PageInfo pageInfo = pageInfoRepository.findByPagePath(path);
        if (pageInfo != null && category.getId().equals(pageInfo.getCategory().getId())) {
            return pageInfo;
        }
        List<PageInfo> pageInfos = pageInfoRepository.findByCategory(category);
        if (pageInfos.isEmpty())
            throw new PageNotFoundException();
        return pageInfos.get(0);
    }

    @Override
    public List<PageInfo> findAll() {
        return pageInfoRepository.findAll();
    }

    @Override
    public void updatePageComponent(PageInfo page, InstalledWidget installedWidget) throws IllegalStateException {
        Layout[] layouts = layoutsForUse(page.getLayout());
        for (PageElement pageElement : layouts) {
            updateComponent(pageElement, installedWidget);
        }
        PageLayout pageLayout = new PageLayout(layouts);
        page.setLayout(pageLayout);
        pageInfoRepository.saveAndFlush(page);
    }

    /**
     * 更新page的component
     *
     * @param element
     * @param installedWidget 更新的控件
     */
    private void updateComponent(PageElement element, InstalledWidget installedWidget) {
        if (element instanceof Layout) {
            Layout layout = (Layout) element;
            for (PageElement pageElement : layout.elements())
                updateComponent(pageElement, installedWidget);
        } else if (element instanceof Component) {
            Component component = (Component) element;
            Widget comWidget = component.getInstalledWidget().getWidget();
            Widget widget = installedWidget.getWidget();
            //同一个控件不同版本才进行更新
            if (comWidget.groupId().equals(widget.groupId()) && comWidget.widgetId().equals(widget.widgetId())
                    && !comWidget.version().equals(widget.version())) {
                component.setInstalledWidget(installedWidget);
                component.setWidgetIdentity(installedWidget.getIdentifier().toString());
            }
        }
    }


}
