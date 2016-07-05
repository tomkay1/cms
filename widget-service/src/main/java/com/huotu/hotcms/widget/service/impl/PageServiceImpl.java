/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.service.ContentsService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hzbc on 2016/6/24.
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private ContentsService contentsService;

    @Autowired
    private PageInfoRepository pageRepository;

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Autowired
    private WidgetResolveService widgetResolveService;

    @Override
    public String generateHTML(Page page, CMSContext context) {
        PageElement[] elements = page.getElements();
        String html = "<div>";
        for (int i = 0, l = elements.length; i < l; i++) {
            html += widgetResolveService.pageElementHTML(elements[i], context);
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
    public void savePage(Page page, Long pageId) throws IOException, URISyntaxException {
        XmlMapper xmlMapper = new XmlMapper();
        String pageXml = xmlMapper.writeValueAsString(page);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageId(pageId);
        pageInfo.setPageSetting(pageXml.getBytes());
        pageInfoRepository.save(pageInfo);
    }

    @Override
    public Page getPage(Long pageId) throws IOException {
        PageInfo pageInfo = pageInfoRepository.findOne(pageId);
        String pageXml = new String(pageInfo.getPageSetting(), "utf-8");
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(pageXml, Page.class);
    }

    @Override
    public void deletePage(Long pageId) throws IOException {
        pageInfoRepository.delete(pageId);
    }

    @Override
    public Page findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException {
        //todo 为了测试模拟的数据，@hzbc 请添加完整实现
        Layout layoutElement = new Layout();
        layoutElement.setValue("12");
        Component component = new Component();
        List<InstalledWidget> installedWidgets = null;
        try {
            String randomType = UUID.randomUUID().toString();
            // 安装一个demo控件
            widgetFactoryService.installWidget("com.huotu.hotcms.widget.pagingWidget", "pagingWidget", "1.0-SNAPSHOT", randomType);
            installedWidgets = widgetFactoryService.widgetList();
            InstalledWidget installedWidget = installedWidgets != null && installedWidgets.size() > 0 ? installedWidgets.get(0) : null;
            assert installedWidget != null;
            String styleId = installedWidget.getWidget().styles() != null ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);
            component.setStyleId(styleId);
            ComponentProperties properties = new ComponentProperties();
            properties.put("pageCount", 20);
            properties.put("pagingTColor", "#000000");
            properties.put("pagingHColor", "#000000");
            component.setProperties(properties);
            layoutElement.setElements(new PageElement[]{component});
            Page page = new Page();
            page.setTitle("test");
            page.setPageIdentity("test001");
            page.setElements(new PageElement[]{layoutElement});
            return page;
        } catch (IOException | FormatException e) {
            throw new IllegalStateException("查找控件列表失败");
        }

    }

    @Override
    public List<Page> getPageList(long siteId) {
        List<PageInfo> pageInfos = pageInfoRepository.findBySiteId(siteId);
        List<Page> pages = new ArrayList<>();
        Page page = null;
        for (PageInfo pageInfo : pageInfos) {
            page = new Page();
            page.setPageIdentity(pageInfo.getPageId() + "");
        }
        return pages;
    }


    @Override
    public Page getClosetContentPage(Category category, String path) {

        return null;
    }


}
