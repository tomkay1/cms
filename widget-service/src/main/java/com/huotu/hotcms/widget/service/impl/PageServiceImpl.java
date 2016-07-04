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
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
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
    private PageInfoRepository pageInfoRepository;

    @Autowired PageRepository pageRepository;


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
    public void deletePage(long ownerId, String pageId) throws IOException {
        pageInfoRepository.delete(pageId);
    }

    @Override
    public Page findBySiteAndPagePath(Site site, String pagePath) throws IllegalStateException {

        return null;
    }

    @Override
    public Page findByPagePath(Site site, String pagePath) throws IOException {
        return null;
    }

    @Override
    public List<Page> getPageList(Site site) {
        List<PageInfo> pageInfos=pageInfoRepository.findBySite(site);
        List<Page> pages=new ArrayList<>();
        Page page=null;
        for(PageInfo pageInfo:pageInfos){
            page=new Page();
            page.setPageIdentity( pageInfo.getPageId());
        }
        return pages;
    }

    @Override
    public com.huotu.hotcms.service.entity.Page findBySiteAndPagePath(Long siteId, String pagePath) throws IOException {
        return pageRepository.findByPagePath(pagePath,siteId);
    }

    @Override
    public Page findByCategoryAndContent(Category category, AbstractContent content) {
        return null;
    }


}
