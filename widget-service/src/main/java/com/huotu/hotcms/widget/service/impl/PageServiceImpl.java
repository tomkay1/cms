/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageRepository;
import com.huotu.hotcms.service.service.ContentsService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzbc on 2016/6/24.
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    ContentsService contentsService;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private WidgetFactoryService widgetFactoryService;


    @Override
    public String generateHTML(Page page, CMSContext context) throws IOException {
        return null;
    }

    @Override
    public void savePage(Page page, String pageId) throws IOException, URISyntaxException {
        XmlMapper xmlMapper=new XmlMapper();
        String pageXml=xmlMapper.writeValueAsString(page);
        PageInfo pageInfo=new PageInfo();
        pageInfo.setPageId(pageId);
        pageInfo.setPageSetting(pageXml.getBytes());
        pageInfoRepository.save(pageInfo);
    }

    @Override
    public Page getPage(String pageId) throws IOException {
        PageInfo pageInfo=pageInfoRepository.findOne(pageId);
        String pageXml=new String(pageInfo.getPageSetting(),"utf-8");
        XmlMapper xmlMapper=new XmlMapper();
        return xmlMapper.readValue(pageXml,Page.class);
    }

    @Override
    public void deletePage(String pageId) throws IOException {
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
            InstalledWidget installedWidget = installedWidgets!=null && installedWidgets.size()>0 ? installedWidgets.get(0): null;
            assert installedWidget != null;
            String styleId  = installedWidget.getWidget().styles() != null ? installedWidget.getWidget().styles()[0].id(): null;
            component.setInstalledWidget(installedWidget);
            component.setStyleId(styleId);
            ComponentProperties properties = new ComponentProperties();
            properties.put("pageCount",20);
            properties.put("pagingTColor","#000000");
            properties.put("pagingHColor","#000000");
            component.setProperties(properties);
            layoutElement.setElements(new PageElement[]{component});
            Page page = new Page();
            page.setTitle("test");
            page.setPageIdentity("test001");
            page.setElements(new PageElement[]{layoutElement});
            return page;
        } catch (IOException |FormatException e) {
            throw new IllegalStateException("查找控件列表失败");
        }
        //todo

        return null;
    }

    @Override
    public List<Page> getPageList(long siteId) {
        List<PageInfo> pageInfos=pageInfoRepository.findBySiteId(siteId);
        List<Page> pages=new ArrayList<>();
        Page page=null;
        for(PageInfo pageInfo:pageInfos){
            page=new Page();
            page.setPageIdentity( pageInfo.getPageId());
        }
        return pages;
    }


}
