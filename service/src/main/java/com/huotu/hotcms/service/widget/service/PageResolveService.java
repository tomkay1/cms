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
            e.printStackTrace();
        }
        return widgetPage;
    }

    public boolean createPageConfigByWidgetPage(WidgetPage widgetPage,Integer customerId,Long siteId,Boolean publish) throws IOException, URISyntaxException {
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
}
