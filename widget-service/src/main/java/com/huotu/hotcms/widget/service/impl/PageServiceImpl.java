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
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.PageRepository;
import com.huotu.hotcms.service.service.ContentsService;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hzbc on 2016/6/24.
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    ContentsService contentsService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ConfigInfo configInfo;
    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;


    @Override
    public String generateHTML(Page page, CMSContext context) throws IOException {
        return null;
    }

    @Override
    public void parsePageToXMlAndSave(Page page, String pageId) throws IOException, URISyntaxException {
        String path = configInfo.getPageConfig(pageId)+".xml";
        XmlMapper xmlMapper=new XmlMapper();
        byte[] pageStream=xmlMapper.writeValueAsString(page).getBytes();
        InputStream inputStream=new ByteArrayInputStream(pageStream);
        resourceService.uploadResource(path, inputStream).httpUrl();
    }

    @Override
    public Page getPageFromXMLConfig(String pageId) throws IOException {
        String path = configInfo.getPageConfig(pageId)+".xml";
        Resource resource=resourceService.getResource(path);
        String xml=StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
        XmlMapper xmlMapper=new XmlMapper();
        return xmlMapper.readValue(xml,Page.class);
    }

    @Override
    public void deletePage(long ownerId, String pageId) throws IOException {
        String path = configInfo.getPageConfig(pageId)+".xml";
        resourceService.deleteResource(path);
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

    }

    @Override
    public com.huotu.hotcms.service.entity.Page findBySiteAndPagePath(Long siteId, String pagePath) throws IOException {
        return pageRepository.findByPagePath(pagePath,siteId);
    }

    @Override
    public Page findByCategoryAndContent(Category category, AbstractContent content) {
        //todo 为了测试模拟的数据，@hzbc 请添加完整实现
        return findBySiteAndPagePath(category.getSite(),"");
    }


}
