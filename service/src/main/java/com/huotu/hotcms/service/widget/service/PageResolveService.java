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
        URI url=null;
        try {
            url=resourceServer.getResource(path);
            if(url!=null) {
                InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
                widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
            }
        } catch (URISyntaxException e) {
            log.error("URISyntaxException 异常-->URI-->" + url.toString() +"path-->"+path+ " message-->" + e.getMessage());
        } catch (MalformedURLException e) {
            log.error("MalformedURL 异常-->"+url.toString()+"path-->"+path+" message-->"+e.getMessage());
        } catch (IOException e) {
            log.error("IO异常-->URI-->" + url.toString() +"path-->"+path+" message-->" + e.getMessage());
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
        URI url=null;
        try {
            url=resourceServer.getResource(path);
            if(url!=null) {
                InputStream inputStream = HttpUtils.getInputStreamByUrl(url.toURL());
                widgetPage = JAXB.unmarshal(inputStream, WidgetPage.class);
            }
        } catch (URISyntaxException e) {
            log.error("URISyntaxException 异常-->URI-->" + url.toString() + "path-->" + path + " message-->" + e.getMessage());
        } catch (MalformedURLException e) {
            log.error("MalformedURL 异常-->" + url.toString() + "path-->" + path + " message-->" + e.getMessage());
        } catch (IOException e) {
            log.error("IO异常-->URI-->" + url.toString() + "path-->" + path + " message-->" + e.getMessage());
        }
        return widgetPage;
    }

    /**
     * <p>
     *     创建页面信息
     * </p>
     * @param page 页面信息对象
     * @param customerId 商户ID
     * @param siteId 站点ID
     * @param publish 是否发布
     * @return 是否成功
     * */
    public boolean createPageAndConfigByWidgetPage(WidgetPage page,Integer customerId,Long siteId,Boolean publish) throws IOException, URISyntaxException {
        CustomPages customPages=new CustomPages();
        if(page!=null) {
            Site site = siteRepository.findOne(siteId);
            if (site != null) {
                customPages.setSite(site);
                customPages.setCustomerId(customerId);
                customPages.setDeleted(false);
                customPages.setDescription(page.getPageDescription());
                customPages.setHome(false);
                customPages.setName(page.getPageName());
                customPages.setOrderWeight(50);
                customPages.setPublish(publish);
                customPages.setCreateTime(LocalDateTime.now());
                customPages = customPagesRepository.save(customPages);
                if (customPages != null) {
                    StringWriter stringWriter = new StringWriter();
                    JAXB.marshal(page, stringWriter);

                    InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("utf-8"));
                    String path = configInfo.getResourcesConfig(customerId, siteId) + "/" + customPages.getId() + ".xml";
                    URI uri = resourceServer.uploadResource(path, inputStream);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     *     创建默认的页面信息
     * </p>
     * @param widgetPage 页面信息对象
     * @param customerId 商户ID
     * @param siteId 站点ID
     * @param config 默认页面对应的名称，比如head或者search 则存储的xml文件为head.xml或者search.xml
     * @return 是否成功
     * */
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

    /**
     * <p>
     *     修改页面信息
     * </p>
     * @param widgetPage 页面信息对象
     * @param customerId 商户ID
     * @param pageId 页面ID
     * @param publish 是否发布
     * @return 是否成功
     * */
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
