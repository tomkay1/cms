/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PageServiceImpl implements PageService {
    @Autowired(required = false)
    SiteRepository siteRepository;

    @Autowired(required = false)
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private WidgetResolveService widgetResolveService;

    @Autowired(required = false)
    private ResourceService resourceService;

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
        PageElement[] elements;
        if (page.getLayout() != null)
            elements = page.getLayout().getElements();
        else
            elements = new PageElement[0];
        writer.append("<div class=\"container\">");
        for (PageElement element : elements) {
            writer.append("<div class=\"row\">");
            widgetResolveService.pageElementHTML(element, context, writer);
            writer.append("</div>");
        }
        writer.append("</div>");
    }


    @Override
    public void savePage(PageModel page, Long pageId) throws IOException {
        PageInfo pageInfo = pageInfoRepository.getOne(pageId);
        pageInfo.setUpdateTime(LocalDateTime.now());
        //删除控件旧的css样式表
        if (pageInfo.getResourceKey() != null) {
            resourceService.deleteResource(pageInfo.getPageCssResourcePath());
        }
        //保存最新控件信息
        String resourceKey = UUID.randomUUID().toString();
        pageInfo.setResourceKey(resourceKey);
        if (page != null && page.getElements() != null)
            pageInfo.setLayout(new PageLayout(page.getElements()));

        // TODO 还需要修改什么么?

//        pageInfo.setPageSetting(pageJson.getBytes());
        pageInfoRepository.save(pageInfo);
        //生成page的css样式表
        PageElement[] elements = pageInfo.getLayout().getElements();
        Path path = Files.createTempFile("tempCss", ".css");
        try {
            try (OutputStream out = Files.newOutputStream(path)) {
                for (PageElement element : elements) {
                    //生成组件css
                    widgetResolveService.componentCSS(CMSContext.RequestContext(), element, out);
                }
                //上传最新的page css样式表到资源服务
                try (InputStream data = Files.newInputStream(path)) {
                    resourceService.uploadResource(pageInfo.getPageCssResourcePath(), data);
                }
            }

        } finally {
            //noinspection ThrowFromFinallyBlock
            Files.deleteIfExists(path);
        }


    }

    @Override
    public PageInfo getPage(Long pageId) throws PageNotFoundException {
        PageInfo pageInfo = pageInfoRepository.findOne(pageId);
        if (pageInfo == null)
            throw new PageNotFoundException("页面ID为" + pageId + "的页面不存在");
        return pageInfo;
    }

    @Override
    public void deletePage(Long pageId) {
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
    public PageInfo getClosestContentPage(Category category, String path) throws IOException, PageNotFoundException {
        PageInfo pageInfo = pageInfoRepository.findByPagePath(path);
        if (pageInfo != null && category.getId().equals(pageInfo.getCategory().getId())) {
            return pageInfo;
        }
        List<PageInfo> pageInfos = pageInfoRepository.findByCategory(category);
        if (pageInfo == null)
            throw new IllegalStateException("没有找到相应page");
        pageInfo = pageInfos.get(0);
        return getPage(pageInfo.getPageId());
    }

    @Override
    public List<PageInfo> findAll() {
        return pageInfoRepository.findAll();
    }

    @Override
    public void updatePageComponent(PageInfo page, InstalledWidget installedWidget) throws IllegalStateException {
        Layout[] pageElements = page.getLayout().getElements();
        for (PageElement pageElement : pageElements) {
            updateComponent(pageElement, installedWidget);
        }
        PageLayout pageLayout = new PageLayout();
        pageLayout.setElements(pageElements);
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
            for (PageElement pageElement : layout.getElements()) {
                updateComponent(pageElement, installedWidget);
            }
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
