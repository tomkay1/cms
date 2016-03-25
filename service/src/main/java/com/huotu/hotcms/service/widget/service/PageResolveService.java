package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.BasicPageType;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.WidgetService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * <p>
 *     页面解析服务
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Component
public class PageResolveService {
    private static final Log log = LogFactory.getLog(PageResolveService.class);

    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CustomPagesRepository customPagesRepository;

    @Autowired
    private SiteRepository siteRepository;

    /**
     * 根据页面配置信息获得WidgetPage对象
     * @param basicPageType
     * @param site
     * @return widgetPage
     * */
    public WidgetPage getWidgetPageByConfig(BasicPageType basicPageType,Site site){
        WidgetPage widgetPage=null;
        String path=configInfo.getResourcesConfig(site.getCustomerId(),site.getSiteId())+"/"+basicPageType.getValue();
        try {
            URI url=resourceServer.getResource(path);
            if(url!=null) {
                InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
                widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return widgetPage;
    }

    /**
     * 根据页面配置信息获得WidgetPage对象
     * @param pageConfigName
     * @param site
     * @return widgetPage
     * */
    public WidgetPage getWidgetPageByConfig(String pageConfigName,Site site){
        WidgetPage widgetPage=null;
        String path=configInfo.getResourcesConfig(site.getCustomerId(),site.getSiteId())+"/"+pageConfigName;
        try {
            URI url=resourceServer.getResource(path);
            if(url!=null) {
                InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
                widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
//            log.error("页面配置文件不存在");
            e.printStackTrace();
        }
        return widgetPage;
    }

    public boolean createPageAndConfigByWidgetPage(WidgetPage widgetPage,Integer customerId,Long siteId,Boolean publish) throws IOException, URISyntaxException {
        CustomPages customPages=new CustomPages();
        if(widgetPage!=null) {
            Site site = siteRepository.findOne(siteId);
            if (site != null) {
                customPages.setSite(site);
                customPages.setCustomerId(customerId);
                customPages.setDeleted(false);
                customPages.setDescription(widgetPage.getPageDescription());
                customPages.setHome(false);
                customPages.setName(widgetPage.getPageName());
                customPages.setOrderWeight(50);
                customPages.setPublish(publish);
                customPages.setCreateTime(LocalDateTime.now());
                customPages = customPagesRepository.save(customPages);
                if (customPages != null) {
                    StringWriter stringWriter = new StringWriter();
                    JAXB.marshal(widgetPage, stringWriter);
                    InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                    String path = configInfo.getResourcesConfig(customerId, siteId) + "/" + customPages.getId() + ".xml";
                    URI uri = resourceServer.uploadResource(path, inputStream);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean createDefaultPageConfigByWidgetPage(WidgetPage widgetPage,Integer customerId,Long siteId,String config) throws IOException, URISyntaxException {
        if(widgetPage!=null) {
            Site site = siteRepository.findOne(siteId);
            if (site != null) {
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(widgetPage, stringWriter);
                InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                String path = configInfo.getResourcesConfig(customerId, siteId) + "/" +config + ".xml";
                URI uri = resourceServer.uploadResource(path, inputStream);
                return true;
            }
        }
        return false;
    }

    public boolean patchPageAndConfigByWidgetPage(WidgetPage widgetPage,Integer customerId,Long pageId,Boolean publish) throws IOException, URISyntaxException {
        CustomPages customPages=customPagesRepository.findOne(pageId);
        if(widgetPage!=null) {
            if (customPages != null) {
                customPages.setDescription(widgetPage.getPageDescription());
                customPages.setName(widgetPage.getPageName());
                customPages.setPublish(publish);
                customPages = customPagesRepository.save(customPages);
                Site site=customPages.getSite();
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(widgetPage, stringWriter);
                InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                String path = configInfo.getResourcesConfig(customerId, site.getSiteId()) + "/" + pageId+ ".xml";
                URI uri = resourceServer.uploadResource(path, inputStream);
                return true;
            }
        }
        return false;
    }
}
