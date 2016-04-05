package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.*;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    private TemplateEngine templateEngine = new TemplateEngine();

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage,Boolean isEdit) throws Exception {
        String htmlTemplate = "";
        if (widgetPage != null) {
            List<WidgetLayout> widgetLayouts = widgetPage.getLayout();
            if (widgetLayouts != null) {
                for (WidgetLayout widgetLayout : widgetLayouts) {
                    if (widgetLayout != null) {
                        htmlTemplate += getWidgetLayoutTemplateByWidgetLayout(widgetLayout,isEdit);
                    }
                }
            }
        }
        return htmlTemplate;
    }

    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase) throws Exception {
        return isWidgetTemplateCached(widgetBase) ? getCachedWidgetTemplate(widgetBase) : getWidgetTemplateFromFile(widgetBase);
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


    public String getWidgetModuleTemplateByWidgetModule(WidgetModule widgetModule) throws Exception {
        String moduleTemplate = "";
        if (widgetModule != null) {
            List<WidgetBase> widgetBases = widgetModule.getWidget();
            for (WidgetBase widgetBase : widgetBases) {
                if (widgetBase != null) {
                    moduleTemplate += getWidgetTemplateByWidgetBase(widgetBase);
                }
            }
        }
        return moduleTemplate;
    }

    /**
     * 解析模块列表下面的组件Html 模版
     */
    public List<String> getWidgetModuleTemplateByWidgetModuleList(List<WidgetModule> widgetModules) throws Exception {
        List<String> moduleTemplateList = new ArrayList<>();
        if (widgetModules != null) {
            for (WidgetModule widgetModule : widgetModules) {
                String moduleTemplate = "";
                moduleTemplate += getWidgetModuleTemplateByWidgetModule(widgetModule);
                moduleTemplateList.add(moduleTemplate);
            }
        }
        return moduleTemplateList;
    }

    public String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout,Boolean isEdit) throws Exception {
        String layoutTemplate = "";
        if (widgetLayout != null) {
            List<String> moduleTemplateList = getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule());
            LayoutEnum layoutEnum=LayoutEnum.valueOf(widgetLayout.getLayoutType());
            layoutTemplate=layoutEnum.getLayoutTemplate(moduleTemplateList,isEdit,widgetLayout.getLayoutId());
        }
        return layoutTemplate;
    }
}
