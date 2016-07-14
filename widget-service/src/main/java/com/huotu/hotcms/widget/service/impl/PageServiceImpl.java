/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hzbc on 2016/6/24.
 */
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
    public String generateHTML(Page page, CMSContext context) {
        PageElement[] elements = page.getElements();
        String html = "<div class=\"container\">";
        for (int i = 0, l = elements.length; i < l; i++) {
            html += "<div class=\"row\">";
            html += widgetResolveService.pageElementHTML(elements[i], context);
            html += "</div>";
        }
        html += "</div>";
        return html;
    }

    @Override
    public void generateHTML(OutputStream outputStream, Page page, CMSContext context) throws IOException {
        String html = generateHTML(page, context);
        byte[] htmlData = html.getBytes();
        outputStream.write(htmlData, 0, htmlData.length);
    }


    @Override
    public void savePage(Page page, Long pageId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String pageJson = objectMapper.writeValueAsString(page);
        PageInfo pageInfo = pageInfoRepository.findOne(pageId);
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            pageInfo.setCreateTime(LocalDateTime.now());
        } else {
            pageInfo.setUpdateTime(LocalDateTime.now());
        }

        //删除控件旧的css样式表
        if (pageInfo.getResourceKey() != null) {
            resourceService.deleteResource(pageInfo.getResourceKey() + "/" + pageInfo.getPageId() + ".css");
        }
        //保存最新控件信息
        String resourceKey = UUID.randomUUID().toString();
        pageInfo.setResourceKey(resourceKey);
        pageInfo.setPageSetting(pageJson.getBytes());
        pageInfoRepository.save(pageInfo);
        //生成page的css样式表
        PageElement[] elements = page.getElements();
        Path path = Files.createTempFile("tempCss", ".css");
        OutputStream out = Files.newOutputStream(path);
        for (int i = 0, l = elements.length; i < l; i++) {
            //生成组件css
            widgetResolveService.componentCSS(CMSContext.RequestContext(), elements[i], out);
        }
        //上传最新的page css样式表到资源服务
        InputStream data = Files.newInputStream(path);
        resourceService.uploadResource(resourceKey + "/" + pageInfo.getPageId() + ".css", data);
        Files.deleteIfExists(path);
    }

    @Override
    public Page getPage(Long pageId) throws IOException {
        PageInfo pageInfo = pageInfoRepository.findOne(pageId);
        String pageJson = new String(pageInfo.getPageSetting(), "utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(pageJson, Page.class);
    }

    @Override
    public void deletePage(Long pageId) {
        pageInfoRepository.delete(pageId);
    }

    @Override
    public Page findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException {
        Page page = null;
        try {
            PageInfo pageInfo = pageInfoRepository.findBySiteAndPagePath(site, pagePath);
            if (pageInfo != null) {
                page = getPage(pageInfo.getPageId());
            }
        } catch (IOException e) {
            throw new IllegalStateException("解析page信息出错");
        }
        return page;
    }

    @Override
    public List<PageInfo> getPageList(Site site) {
        return pageInfoRepository.findBySite(site);
    }

    @Override
    public Page getClosestContentPage(Category category, String path) throws IOException {
        PageInfo pageInfo = pageInfoRepository.findByPagePath(path);
        if (pageInfo != null && category.getId().equals(pageInfo.getCategory().getId())) {
            return getPage(pageInfo.getPageId());
        }
        List<PageInfo> pageInfos = pageInfoRepository.findByCategory(category);
        if (pageInfo == null)
            throw new IllegalStateException("没有找到相应page");
        pageInfo = pageInfos.get(0);
        return getPage(pageInfo.getPageId());
    }

    @Override
    public List<Page> findAll() throws IOException {
        List<PageInfo> pageInfoList = pageInfoRepository.findAll();
        List<Page> pageList = null;
        if (pageInfoList != null && pageInfoList.size() > 0) {
            pageList = new ArrayList<>();
            for (int i = 0, l = pageInfoList.size(); i < l; i++) {
                PageInfo pageInfo = pageInfoList.get(i);
                pageList.add(getPage(pageInfo.getPageId()));
            }
        }
        return pageList;
    }

    @Override
    public void updatePageComponent(Page page, InstalledWidget installedWidget) throws IllegalStateException {
        PageElement[] pageElements = page.getElements();
        for (PageElement pageElement : pageElements) {
            updateComponent(pageElement, installedWidget);
        }

        try {
            savePage(page, page.getPageIdentity());
        } catch (IOException e) {
            throw new IllegalStateException("更新控件组件，保存界面错误" + e.getMessage());
        }
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
