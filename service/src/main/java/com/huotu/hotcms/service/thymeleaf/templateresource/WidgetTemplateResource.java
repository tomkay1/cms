/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.templateresource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.service.FailPageService;
import com.huotu.hotcms.service.service.impl.CustomPagesServiceImpl;
import com.huotu.hotcms.service.service.impl.FailPageServiceImpl;
import com.huotu.hotcms.service.service.impl.SiteServiceImpl;
import com.huotu.hotcms.service.util.DesEncryption;
import com.huotu.hotcms.service.util.StringUtil;
import com.huotu.hotcms.service.widget.service.PageResolveService;
import com.huotu.hotcms.service.widget.service.PageResourceService;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.util.EncryptUtil;
import org.apache.logging.log4j.core.jmx.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.Validate;
import sun.security.krb5.EncryptedData;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;

/**
 * 组件模板资源
 * Created by cwb on 2016/3/16.
 */
public class WidgetTemplateResource implements ITemplateResource {
    private static final Log log = LogFactory.getLog(WidgetTemplateResource.class);
    private String location;//编辑格式如下{siteId}_{pageConfigName}.shtml
    private final String EDIT_JAVASCRIPT = "<script>seajs.use([\"widgetTooBar\",\"cmsQueue\",\"widgetData\"]);</script>";
    //    private final String EDIT_JAVASCRIPT="<script>seajs.use([\"widgetTooBar\",\"cmsQueue\"]);</script>";
    private final String version = "1.1";

    private HttpServletRequest request;

    private String WIDGET_RESOURCES = " " +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.base.css?v={version}\"/>\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.set.css?v={version}\"/>\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.layout.css?v={version}\"/>\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.design.css?v={version}\"/>\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/Advanced-search.css?v={version}\"/>";


    private final String EDIT_HTML_BOX = "<!DOCTYPE html><html>\n" +
            "<head>\n" +
            "    <title>店铺装修-可视化编辑</title>\n" +
            "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
            "    <meta name=\"widget\" content=\"{config_widgetJson}\"/>\n" +
            "    <meta name=\"exists\" content=\"{config_existsPage}\"/>\n" +
            " <link  href=\"/assets/css/main.css\"  type=\"text/css\" rel=\"stylesheet\"/>\n%s" +
            " <link href=\"/assets/libs/layer/skin/layer.css\" rel=\"stylesheet\"/>" +
            "    <script src=\"/assets/seajs/sea.js\"></script>\n" +
            "    <script src=\"/assets/seajs/config.js\"></script>\n" +
            "</head>\n" +
            "<body th:style=\"'background-color:'+${pageBackGround}+'; background: url('+${pageBackImage}+')'+((${pageBackRepeat}=='no-repeat')?' no-repeat':(' '+${pageHorizontalDistance}+${pageHorizontalUnit}+' '+${pageVerticalDistance}+${pageVerticalUnit}+' '+${pageBackRepeat}))\">" +
            "%s" +
            "<div class=\"layout-area HOT-layout-add js-layout js-layout-add\" id=\"insertToLayout\">\n" +
            "    <div class=\"layout\">\n" +
            "      <a href=\"javascript:;\" class=\"link-add-layout\" id=\"addLayout\">添加布局</a>\n" +
            "    </div>\n" +
            "  </div>" +
            "%s" +
            "</body>" +
            "</html>";

    private String BROWSE_RESOURCES = "<link rel=\"stylesheet\" type=\"text/css\" href=\"{PREFIX}/css/mall.global.css?v={version}\"/>";

    private String BROWSE_HTML_BOX = "<!DOCTYPE html><html>\n" +
            "<head>\n" +
            "    <title>商城首页</title>\n" +
            "    <meta charset=\"UTF-8\" content=\"text/html\"/>\n" +
            "    <script src=\"{contentPath}/assets/seajs/sea.js\"></script>\n" +
            "    <script src=\"{contentPath}/assets/seajs/config.js\"></script>\n" +
            "%s\n" +
            "</head>" +
            "<body th:style=\"'background-color:'+${pageBackGround}+'; background: url('+${pageBackImage}+')'+((${pageBackRepeat}=='no-repeat')?' no-repeat':(' '+${pageHorizontalDistance}+${pageHorizontalUnit}+' '+${pageVerticalDistance}+${pageVerticalUnit}+' '+${pageBackRepeat}))\">" +
            "%s" +
            "<script>seajs.use([\"main\"]);</script>"+
            "</body>" +
            "</html>";

    private PageResourceService pageResourceService;

    private PageResolveService pageResolveService;

    private CustomPagesServiceImpl customPagesService;

    private FailPageService failPageService;


    private SiteServiceImpl siteService;
    private ThymeleafViewResolver widgetViewResolver;
    private String URI_PREFIX;
    private TemplateEngine templateEngine;

    //  private final Resource resource;
    private final String characterEncoding;

    private ApplicationContext applicationContext;

    public WidgetTemplateResource(){
        this.characterEncoding="utf-8";
    }

    public WidgetTemplateResource(final ApplicationContext applicationContext, final String location, final String characterEncoding) {
        super();
        request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.BROWSE_HTML_BOX=this.BROWSE_HTML_BOX.replace("{contentPath}",request.getContextPath());
        URI_PREFIX = this.getURI_PREFIX(applicationContext);
        this.applicationContext=applicationContext;
        Validate.notNull(applicationContext, "Application Context cannot be null");
        Validate.notEmpty(location, "Resource Location cannot be null or empty");
        pageResourceService = (PageResourceService) applicationContext.getBean("pageResourceService");
        pageResolveService = (PageResolveService) applicationContext.getBean("pageResolveService");
        siteService = (SiteServiceImpl) applicationContext.getBean("siteServiceImpl");
        widgetViewResolver = (ThymeleafViewResolver) applicationContext.getBean("widgetViewResolver");
        customPagesService = (CustomPagesServiceImpl) applicationContext.getBean("customPagesServiceImpl");
        failPageService = (FailPageServiceImpl) applicationContext.getBean("failPageServiceImpl");
        this.location = location;
        this.characterEncoding = characterEncoding;
        this.WIDGET_RESOURCES = this.WIDGET_RESOURCES.replace("{PREFIX}", this.URI_PREFIX);
        this.WIDGET_RESOURCES = this.WIDGET_RESOURCES.replace("{version}", this.version);
    }

    /**
     * <p>获得组件资源根Uri</p>
     * @param applicationContext 程序上下文
     * */
    private String getURI_PREFIX(ApplicationContext applicationContext) {
        return getURI_PREFIXByEnvironment(applicationContext.getEnvironment());
    }

    private String getURI_PREFIXByEnvironment(Environment environment){
        String uriPrefix = environment.getProperty("cms.resourcesPrefix", (String) null);
        if (uriPrefix == null) {
            uriPrefix = "http://cms.huobanj.cn";
        }
        return uriPrefix;
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

    private Long getSiteId() {
        if (this.location != null) {
            if (this.location.indexOf("_") > 0) {
                return Long.valueOf(this.location.substring(0, this.location.indexOf("_")));
            }
        }
        return null;
    }

    private Boolean isBrowse() {
        if (this.location != null) {
            return this.location.contains(".cshtml");
        }
        return false;
    }

    private String getPageConfigNameContainXml() {
        String configName = this.getPageConfigName();
        if (StringUtils.isEmpty(configName)) {
            return null;
        }
        return configName + ".xml";
    }

    private String getPageConfigName() {
        String suffix = "";
        if (isBrowse()) {
            suffix = ".cshtml";
        } else {
            suffix = ".shtml";
        }
        if (this.location.indexOf("_") > 0 && this.location.indexOf(suffix) > 0) {
            return this.location.substring(this.location.indexOf("_") + 1, this.location.indexOf(suffix));
        }
        return null;
    }

    private Boolean isExists(String pageConfigName) {
        try {
            CustomPages customPages = customPagesService.getCustomPages(Long.valueOf(pageConfigName));
            return customPages != null;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }

    /**
     * <p>
     *     获得控件主体所需要的静态资源应用,css、js等的引用
     * </p>
     * */
    public String getHtmlHeadStaticResources(Environment environment){
        this.URI_PREFIX = this.getURI_PREFIXByEnvironment(environment);
        String cssResources=this.WIDGET_RESOURCES + BROWSE_RESOURCES;
        cssResources= cssResources.replace("{version}", this.version);
        cssResources= cssResources.replace("{PREFIX}", this.URI_PREFIX);
        return cssResources;
    }

    private String getBrowseTemplate(WidgetPage widgetPage, Site site) throws Exception {
        String htmlTemplate = pageResourceService.getHtmlTemplateByWidgetPage(widgetPage, false, site);
        if (widgetPage.getPageEnabledHead() != null) {
            if (widgetPage.getPageEnabledHead()) {//取用头部
                String commonHeader = pageResourceService.getHeaderTemplateBySite(site);
                if (null != commonHeader) {
                    htmlTemplate = commonHeader + htmlTemplate;
                }
            }
        }
        this.BROWSE_RESOURCES = this.BROWSE_RESOURCES.replace("{PREFIX}", this.URI_PREFIX);
        this.BROWSE_RESOURCES = this.BROWSE_RESOURCES.replace("{version}", this.version);
        String browseTemplate=pageResourceService.getHeaderTemplate(this.BROWSE_HTML_BOX,widgetPage,site);
        htmlTemplate = String.format(browseTemplate, this.WIDGET_RESOURCES + BROWSE_RESOURCES, htmlTemplate);
        return htmlTemplate;
    }

    private String getEditTemplate(WidgetPage widgetPage, String pageConfigName, Site site) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String htmlTemplate = pageResourceService.getHtmlTemplateByWidgetPage(widgetPage, true, site);
        String editTemplate=pageResourceService.getHeaderTemplate(this.EDIT_HTML_BOX,widgetPage,site);
        htmlTemplate = String.format(editTemplate, this.WIDGET_RESOURCES, htmlTemplate, EDIT_JAVASCRIPT);
        htmlTemplate = htmlTemplate.replace("{config_existsPage}", (isExists(pageConfigName) ? pageConfigName : "0"));
        String widgetJson = mapper.writeValueAsString(widgetPage);
        htmlTemplate = htmlTemplate.replace("{config_widgetJson}", DesEncryption.encryptData(widgetJson));
        return htmlTemplate;
    }

    private String getTemplate(WidgetPage widgetPage, Site site) throws Exception {
        String pageConfigName = this.getPageConfigName();
        String htmlTemplate = null;
        if (isBrowse()) {
            htmlTemplate = getBrowseTemplate(widgetPage, site);
        } else {
            htmlTemplate = getEditTemplate(widgetPage, pageConfigName, site);
        }
        return htmlTemplate;
    }

    @Override
    public Reader reader() throws IOException {
        Long siteId = this.getSiteId();
        String htmlTemplate = "";
        WidgetPage widgetPage = null;
        String pageConfigNameContainXml = null;
        if (siteId != null) {
            pageConfigNameContainXml = this.getPageConfigNameContainXml();
            Site site = siteService.getSite(siteId);
            widgetPage = pageResolveService.getWidgetPageByConfig(pageConfigNameContainXml, site);
            try {
                htmlTemplate = getTemplate(widgetPage, site);
            } catch (Exception e) {
                if(isBrowse()){
                    htmlTemplate = failPageService.getFailPageTemplate(applicationContext,PageErrorType.BUDDING_500);
                }else{
                    log.error(e.getMessage());
                }
            }
        }
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
        final String basePath = (path.charAt(path.length() - 1) == '/' ? path.substring(0, path.length() - 1) : path);

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
