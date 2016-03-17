package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.BasicPageType;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

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

}
