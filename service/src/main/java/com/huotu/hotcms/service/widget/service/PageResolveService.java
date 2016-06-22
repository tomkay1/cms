/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.BasicPageType;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.SerialUtil;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * <p>
 * 页面解析服务
 * </p>
 *
 * @author xhl
 * @since 1.2
 */
@Component
public class PageResolveService {
    private static final Log log = LogFactory.getLog(PageResolveService.class);

    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private CustomPagesRepository customPagesRepository;

    @Autowired
    private SiteRepository siteRepository;

    /**
     * 根据页面配置信息获得WidgetPage对象
     *
     * @param basicPageType
     * @param site
     * @return widgetPage
     */
    public WidgetPage getWidgetPageByConfig(BasicPageType basicPageType, Site site) {
        String path = configInfo.getResourcesConfig(site) + "/" + basicPageType.getValue();
        return aboutWidgetPage(path);
    }

    /**
     * 根据页面配置信息获得WidgetPage对象
     *
     * @param pageConfigName
     * @param site
     * @return widgetPage
     */
    public WidgetPage getWidgetPageByConfig(String pageConfigName, Site site) {
        String path = configInfo.getResourcesConfig(site) + "/" + pageConfigName;
        return aboutWidgetPage(path);
    }

    private WidgetPage aboutWidgetPage(String path) {
        try {
            URL url = resourceService.getResource(path).httpUrl();
            InputStream inputStream = HttpUtils.getInputStreamByUrl(url);
            return JAXB.unmarshal(inputStream, WidgetPage.class);
        } catch (MalformedURLException e) {
            log.error("MalformedURL 异常--> path-->" + path + " message-->" + e.getMessage());
        } catch (IOException e) {
            log.error("IO异常-->URI--> path-->" + path + " message-->" + e.getMessage());
        }
        return null;
    }

    /**
     * <p>
     * 创建页面信息
     * </p>
     *
     * @param page    页面信息对象
     * @param siteId  站点ID
     * @param publish 是否发布
     * @return 是否成功
     */
    public boolean createPageAndConfigByWidgetPage(WidgetPage page, Long siteId, Boolean publish)
            throws IOException, URISyntaxException {
        CustomPages customPages = new CustomPages();
        if (page != null) {
            Site site = siteRepository.findOne(siteId);
            if (site != null) {
                customPages.setSite(site);
//                customPages.setCustomerId(customerId);
                customPages.setDeleted(false);
                customPages.setDescription(page.getPageDescription());
                customPages.setHome(false);
                customPages.setName(page.getPageName());
                customPages.setOrderWeight(50);
                customPages.setPublish(publish);
                //site_{siteID}_serial
                customPages.setSerial(SerialUtil.formartSerial(site));
                customPages.setCreateTime(LocalDateTime.now());
                customPages = customPagesRepository.save(customPages);
                if (customPages != null) {
                    StringWriter stringWriter = new StringWriter();
                    JAXB.marshal(page, stringWriter);
                    InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                    String path;
                    path = configInfo.getResourcesConfig(site) + "/" + customPages.getId() + ".xml";
                    resourceService.uploadResource(path, inputStream);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 创建默认的页面信息
     * </p>
     *
     * @param widgetPage 页面信息对象
     * @param siteId     站点ID
     * @param config     默认页面对应的名称，比如head或者search 则存储的xml文件为head.xml或者search.xml
     * @return 是否成功
     */
    public boolean createDefaultPageConfigByWidgetPage(WidgetPage widgetPage, Long siteId
            , String config) throws IOException, URISyntaxException {
        if (widgetPage != null) {
            Site site = siteRepository.findOne(siteId);
            if (site != null) {
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(widgetPage, stringWriter);
                InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                String path = configInfo.getResourcesConfig(site) + "/" + config + ".xml";
                URI uri = resourceService.uploadResource(path, inputStream).httpUrl().toURI();
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * 修改页面信息
     * </p>
     *
     * @param widgetPage 页面信息对象
     * @param pageId     页面ID
     * @param publish    是否发布
     * @return 是否成功
     */
    public boolean patchPageAndConfigByWidgetPage(WidgetPage widgetPage, Long pageId, Boolean publish) throws IOException, URISyntaxException {
        CustomPages customPages = customPagesRepository.findOne(pageId);
        if (widgetPage != null) {
            if (customPages != null) {
                customPages.setDescription(widgetPage.getPageDescription());
                customPages.setName(widgetPage.getPageName());
                customPages.setPublish(publish);
                customPages = customPagesRepository.save(customPages);
                Site site = customPages.getSite();
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(widgetPage, stringWriter);
                InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                String path = configInfo.getResourcesConfig(site) + "/" + pageId + ".xml";
                URI uri = resourceService.uploadResource(path, inputStream).httpUrl().toURI();
                return true;
            }
        }
        return false;
    }
}
