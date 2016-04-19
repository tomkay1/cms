package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.*;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.ReflectionUtil;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

/**
 * <p>
 * 页面解析服务
 * </p>
 *
 * @author xhl
 * @since 1.2
 */
@Component
public class PageResourceService {
    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PageResolveService pageResolveService;

    @Autowired
    private WidgetResolveService widgetResolveService;

    private TemplateEngine templateEngine=new TemplateEngine();

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage,Boolean isEdit,Site site) throws Exception {
        String htmlTemplate = "";
        if (widgetPage != null) {
            List<WidgetLayout> widgetLayouts = widgetPage.getLayout();
            if (widgetLayouts != null) {
                for (WidgetLayout widgetLayout : widgetLayouts) {
                    if (widgetLayout != null) {
                        htmlTemplate += getWidgetLayoutTemplateByWidgetLayout(widgetLayout,isEdit,site);
                    }
                }
            }
        }
        return htmlTemplate;
    }

    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase) throws Exception {
        if (redisService.isContent())
            return isWidgetTemplateCached(widgetBase) ? getCachedWidgetTemplate(widgetBase) : getWidgetTemplateFromFile(widgetBase);
        else
            return getWidgetTemplateFromFile(widgetBase);
    }

    public String getWidgetTemplateResolveByWidgetBase(WidgetBase widgetBase,Site site) throws Exception {
        String widgetHtml=getWidgetTemplateByWidgetBase(widgetBase);
        widgetHtml=widgetResolveService.widgetBriefView(widgetHtml,widgetBase,site);
        return widgetHtml;
    }

    private String getCachedWidgetTemplate(WidgetBase widgetBase) {
        return redisService.findByWidgetId(widgetBase.getId());
    }

    private boolean isWidgetTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetExists(widgetBase.getId());
    }

    private String getWidgetTemplateFromFile(WidgetBase widgetBase) throws Exception{
        URL url = resourceServer.getResource(widgetBase.getWidgetUri()).toURL();
//        List<WidgetProperty> widgetProperties = widgetBase.getProperty();
//        Map widgetProperties=widgetBase.getProperty();
        String widgetTemplate = HttpUtils.getHtmlByUrl(url);
//        if (widgetProperties != null) {
//            for (WidgetProperty widgetProperty : widgetProperties) {
//                if (widgetProperty != null) {
//                    widgetTemplate = widgetTemplate.replace("{" + widgetProperty.getName() + "}", widgetProperty.getValue());
//                }
//            }
//        }
        if(redisService.isContent()) {
            redisService.saveWidget(widgetBase.getId(), widgetTemplate);
        }
        return widgetTemplate;
    }


    public String getWidgetModuleTemplateByWidgetModule(WidgetModule widgetModule,Boolean isEdit,Site site) throws Exception {
        String moduleTemplate = "";
        if (widgetModule != null) {
            List<WidgetBase> widgetBases = widgetModule.getWidget();
            for (WidgetBase widgetBase : widgetBases) {
                if (widgetBase != null) {
                    widgetBase.setEdit(isEdit);
                    if(widgetBase.getGuid()==null){
                        widgetBase.setGuid(UUID.randomUUID().toString());
                    }
                    String template=getWidgetTemplateResolveByWidgetBase(widgetBase,site);
                    moduleTemplate +=template;
                }
            }
        }
        return moduleTemplate;
    }

    /**
     * 解析模块列表下面的组件Html 模版
     */
    public List<String> getWidgetModuleTemplateByWidgetModuleList(List<WidgetModule> widgetModules,Boolean isEdit,Site site) throws Exception {
        List<String> moduleTemplateList = new ArrayList<>();
        if (widgetModules != null) {
            for (WidgetModule widgetModule : widgetModules) {
                if(widgetModule.getWidget()!=null) {
                    String moduleTemplate = "";
                    moduleTemplate += getWidgetModuleTemplateByWidgetModule(widgetModule, isEdit,site);
                    moduleTemplateList.add(moduleTemplate);
                }
            }
        }
        return moduleTemplateList;
    }

    public String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout,Boolean isEdit,Site site) throws Exception {
        String layoutTemplate = "";
        if (widgetLayout != null) {
            List<String> moduleTemplateList = getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule(),isEdit,site);
            LayoutEnum layoutEnum=LayoutEnum.valueOf(widgetLayout.getLayoutType());
            layoutTemplate=layoutEnum.getLayoutTemplate(moduleTemplateList,isEdit,widgetLayout.getLayoutId());
        }
        return layoutTemplate;
    }

    public String getHeaderTemplaeBySite(Site site) throws Exception {
        WidgetPage widgetPage=pageResolveService.getWidgetPageByConfig("head.xml", site);
        if(widgetPage!=null){
           return getHtmlTemplateByWidgetPage(widgetPage, false,site);
        }
        return null;
    }

    public String getHeaderTemplate(String resources,WidgetPage widgetPage){
        try{
            Context context=new Context(Locale.CHINA, ReflectionUtil.getFieldList(widgetPage));
            StringWriter writer = new StringWriter();
            templateEngine.process(resources,context,writer);
            return writer.toString();
        }catch (Exception ex){
            return resources;
        }
    }
}
