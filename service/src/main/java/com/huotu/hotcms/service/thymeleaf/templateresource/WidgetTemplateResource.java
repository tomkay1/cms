/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.templateresource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.repository.CustomPagesRepository;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.service.impl.CustomPagesServiceImpl;
import com.huotu.hotcms.service.service.impl.SiteServiceImpl;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.Validate;

import java.io.*;

/**
 * 组件模板资源
 * Created by cwb on 2016/3/16.
 */
public class WidgetTemplateResource implements ITemplateResource {
    private String location;//格式如下{siteId}_{pageConfigName}.shtml
    private final String SERVICE_JAVASCRIPT="<script>seajs.use([\"widgetTooBar\"]);</script>";
    private final String SERVICE_HTML_BOX="<!DOCTYPE html><html>\n" +
            "<head>\n" +
            "    <title>店铺装修-可视化编辑</title>\n" +
            "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
            "    <meta name=\"widget\" content=\"{config_widgetJson}\"/>\n" +
            "    <meta name=\"exists\" content=\"{config_existsPage}\"/>\n" +
            " <link  href=\"/assets/css/main.css\"  type=\"text/css\" rel=\"stylesheet\"/>\n" +
            " <link href=\"/assets/libs/layer/skin/layer.css\" rel=\"stylesheet\"/>" +
            "    <script src=\"/assets/seajs/sea.js\"></script>\n" +
            "    <script src=\"/assets/seajs/config.js\"></script>\n" +
            "</head>\n" +
            "<body style=\"background:#ffffff;\">" +
            "%s" +
            "%s" +
            "</body>" +
            "</html>";
    private final String WEB_HTML_BOX="<!DOCTYPE html><html>\n" +
            "<head>\n" +
            "    <title>店铺装修-火图CMS内容管理系统</title>\n" +
            "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
            "</head>\n" +
            "<body>" +
            "%s" +
            "%s" +
            "</body>" +
            "</html>";

    private PageResourceService pageResourceService;

    private PageResolveService pageResolveService;

    private CustomPagesServiceImpl customPagesService;

    private SiteServiceImpl siteService ;

//  private final Resource resource;
    private final String characterEncoding;

    public WidgetTemplateResource(final ApplicationContext applicationContext, final String location, final String characterEncoding) {
        super();
        Validate.notNull(applicationContext, "Application Context cannot be null");
        Validate.notEmpty(location, "Resource Location cannot be null or empty");
        pageResourceService = (PageResourceService) applicationContext.getBean("pageResourceService");
        pageResolveService = (PageResolveService) applicationContext.getBean("pageResolveService");
        siteService = (SiteServiceImpl) applicationContext.getBean("siteServiceImpl");
        customPagesService=(CustomPagesServiceImpl) applicationContext.getBean("customPagesServiceImpl");
        this.location=location;
        this.characterEncoding = characterEncoding;
    }

    public WidgetTemplateResource(final Resource resource,final String characterEncoding) {

        super();

        Validate.notNull(resource, "Resource cannot be null");
        // Character encoding CAN be null (system default will be used)

//        this.resource = resource;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public String getDescription() {
//        return this.resource.getDescription();
        return "";
    }

    @Override
    public String getBaseName() {
//        return computeBaseName(this.resource.getFilename());
        return "";
    }

    @Override
    public boolean exists() {
//        return this.resource.exists();
        return true;
    }

    private Long getSiteId(){
        if(this.location!=null){
            if(this.location.indexOf("_")>0){
                return Long.valueOf(this.location.substring(0,this.location.indexOf("_")));
            }
        }
        return null;
    }

    private String getPageConfigNameContainXml(){
        String configName=this.getPageConfigName();
        if(StringUtils.isEmpty(configName)){
            return null;
        }
        return configName+".xml";
    }

    private String getPageConfigName(){
        if(this.location!=null){
            if(this.location.indexOf("_")>0&&this.location.indexOf(".shtml")>0){
                return this.location.substring(this.location.indexOf("_")+1,this.location.indexOf(".shtml"));
            }
        }
        return null;
    }

    private Boolean isExists(String pageConfigName){
        try {
            CustomPages customPages = customPagesService.getCustomPages(Long.valueOf(pageConfigName));
            return  customPages!=null;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return false;
    }

    @Override
    public Reader reader() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        Long siteId=this.getSiteId();
        String htmlTemplate = "";
        WidgetPage widgetPage=null;
        String pageConfigName=null;
        String pageConfigNameContainXml=null;
        if(siteId!=null) {
            pageConfigName=this.getPageConfigName();
            pageConfigNameContainXml = this.getPageConfigNameContainXml();
            boolean isExists=false;
            Site site = siteService.getSite(siteId);
            widgetPage= pageResolveService.getWidgetPageByConfig(pageConfigNameContainXml, site);
            try {
                htmlTemplate = pageResourceService.getHtmlTemplateByWidgetPage(widgetPage);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        htmlTemplate=String.format(this.SERVICE_HTML_BOX,htmlTemplate,SERVICE_JAVASCRIPT);
//        htmlTemplate=htmlTemplate.replace("{config_widgetJson}",mapper.writeValueAsString(widgetPage));
        htmlTemplate=htmlTemplate.replace("{config_existsPage}",(isExists(pageConfigName)?pageConfigName:"0"));
        return new StringReader(htmlTemplate);
    }

    @Override
    public ITemplateResource relative(final String relativeLocation) throws IOException {
//        return new WidgetTemplateResource(this.resource.createRelative(relativeLocation), this.characterEncoding);
        return null;
    }

    static String computeBaseName(final String path) {
        if (path == null) {
            return null;
        }

        // First remove a trailing '/' if it exists
        final String basePath = (path.charAt(path.length() - 1) == '/'? path.substring(0,path.length() - 1) : path);

        final int slashPos = basePath.lastIndexOf('/');
        if (slashPos != -1) {
            final int dotPos = basePath.lastIndexOf('.');
            if (dotPos != -1 && dotPos > slashPos + 1) {
                return basePath.substring(slashPos + 1, dotPos);
            }
            return basePath.substring(slashPos + 1);
        }

        return basePath;

    }
}
