package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.*;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private WidgetResolveService widgetResolveService;

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage,Boolean isEdit,ITemplateEngine templateEngine) throws Exception {
        String htmlTemplate = "";
        if (widgetPage != null) {
            List<WidgetLayout> widgetLayouts = widgetPage.getLayout();
            if (widgetLayouts != null) {
                for (WidgetLayout widgetLayout : widgetLayouts) {
                    if (widgetLayout != null) {
                        htmlTemplate += getWidgetLayoutTemplateByWidgetLayout(widgetLayout,isEdit,templateEngine);
                    }
                }
            }
        }
        return htmlTemplate;
    }

    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase) throws Exception {
        return isWidgetTemplateCached(widgetBase) ? getCachedWidgetTemplate(widgetBase) : getWidgetTemplateFromFile(widgetBase);
    }

    public String getWidgetTemplateResolveByWidgetBase(WidgetBase widgetBase,ITemplateEngine templateEngine) throws Exception {
        String widgetHtml=getWidgetTemplateByWidgetBase(widgetBase);
        widgetHtml=widgetResolveService.widgetBriefView(widgetHtml,widgetBase,templateEngine);
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
        redisService.saveWidget(widgetBase.getId(),widgetTemplate);
        return widgetTemplate;
    }


    public String getWidgetModuleTemplateByWidgetModule(WidgetModule widgetModule,Boolean isEdit,ITemplateEngine templateEngine) throws Exception {
        String moduleTemplate = "";
        if (widgetModule != null) {
            List<WidgetBase> widgetBases = widgetModule.getWidget();
            for (WidgetBase widgetBase : widgetBases) {
                if (widgetBase != null) {
                    widgetBase.setEdit(isEdit);
                    String template=getWidgetTemplateResolveByWidgetBase(widgetBase,templateEngine);
                    moduleTemplate +=template;
                }
            }
        }
        return moduleTemplate;
    }

    /**
     * 解析模块列表下面的组件Html 模版
     */
    public List<String> getWidgetModuleTemplateByWidgetModuleList(List<WidgetModule> widgetModules,Boolean isEdit,ITemplateEngine templateEngine) throws Exception {
        List<String> moduleTemplateList = new ArrayList<>();
        if (widgetModules != null) {
            for (WidgetModule widgetModule : widgetModules) {
                if(widgetModule.getWidget()!=null) {
                    String moduleTemplate = "";
                    moduleTemplate += getWidgetModuleTemplateByWidgetModule(widgetModule, isEdit,templateEngine);
                    moduleTemplateList.add(moduleTemplate);
                }
            }
        }
        return moduleTemplateList;
    }

    public String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout,Boolean isEdit,ITemplateEngine templateEngine) throws Exception {
        String layoutTemplate = "";
        if (widgetLayout != null) {
            List<String> moduleTemplateList = getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule(),isEdit,templateEngine);
            LayoutEnum layoutEnum=LayoutEnum.valueOf(widgetLayout.getLayoutType());
            layoutTemplate=layoutEnum.getLayoutTemplate(moduleTemplateList,isEdit,widgetLayout.getLayoutId());
        }
        return layoutTemplate;
    }
}
