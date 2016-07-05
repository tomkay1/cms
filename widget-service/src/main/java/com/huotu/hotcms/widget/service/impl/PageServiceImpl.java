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
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by hzbc on 2016/6/24.
 */
@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private PageInfoRepository pageInfoRepository;
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
    public void savePage(Page page, Long pageId) throws IOException {
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
        PageInfo pageInfo = pageInfoRepository.findByCategory_SiteAndPagePath(site, pagePath);
        try {
            return getPage(pageInfo.getPageId());
        } catch (IOException e) {
            throw new IllegalStateException("解析page信息出错:" + e.getMessage());
        }
    }

    @Override
    public List<PageInfo> getPageList(Site site) {
        return pageInfoRepository.findByCategory_Site(site);
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


}
