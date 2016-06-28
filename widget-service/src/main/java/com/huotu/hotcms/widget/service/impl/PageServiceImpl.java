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
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.page.Page;
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

/**
 * Created by hzbc on 2016/6/24.
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ConfigInfo configInfo;


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


}
