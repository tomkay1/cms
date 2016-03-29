package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.*;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage) throws Exception {
        String htmlTemplate = "";
        if (widgetPage != null) {
            List<WidgetLayout> widgetLayouts = widgetPage.getLayout();
            if (widgetLayouts != null) {
                for (WidgetLayout widgetLayout : widgetLayouts) {
                    if (widgetLayout != null) {
                        htmlTemplate += getWidgetLayoutTemplateByWidgetLayout(widgetLayout);
                    }
                }
            }
        }
        return htmlTemplate;
    }

    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase) throws Exception {
        if(redisService.isConnected()){
            return isWidgetTemplateCached(widgetBase) ? getCachedWidgetTemplate(widgetBase) : getWidgetTemplateFromFile(widgetBase);
        }else{
            return  getWidgetTemplateFromFile(widgetBase);
        }
    }

    private String getCachedWidgetTemplate(WidgetBase widgetBase) {
        return redisService.findByWidgetId(widgetBase.getId());
    }

    private boolean isWidgetTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetExists(widgetBase.getId());
    }

    private String getWidgetTemplateFromFile(WidgetBase widgetBase) throws Exception{
        URL url = resourceServer.getResource(widgetBase.getWidgetUri()).toURL();
        List<WidgetProperty> widgetProperties = widgetBase.getProperty();
        String widgetTemplate = HttpUtils.getHtmlByUrl(url);
        if (widgetProperties != null) {
            for (WidgetProperty widgetProperty : widgetProperties) {
                if (widgetProperty != null) {
                    widgetTemplate = widgetTemplate.replace("{" + widgetProperty.getName() + "}", widgetProperty.getValue());
                }
            }
        }
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

    public String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout) throws Exception {
        String layoutTemplate = "";
        if (widgetLayout != null) {
            List<String> moduleTemplateList = getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule());
            switch (widgetLayout.getLayoutType()) {
                case 0:
                    layoutTemplate=LayoutEnum.THREE_COLUMN_LAYOUT_190x590x190.getLayoutTemplate(moduleTemplateList);
                    break;
                case 1:
                    layoutTemplate=LayoutEnum.WITHOUT_COLUMN_LAYOUT_990.getLayoutTemplate(moduleTemplateList);
                    break;
                case 2:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_190x790.getLayoutTemplate(moduleTemplateList);
                    break;
                case 3:
                    layoutTemplate=LayoutEnum.RIGHT_PART_LAYOUT_190x390x390.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.RIGHT_PART_LAYOUT_190x390x390.getLayoutTemplate(), templateParam0, templateParam1, templateParam2);
                    break;
                case 4:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_790x190.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_790x190.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 5:
                    layoutTemplate=LayoutEnum.LEFT_PART_LAYOUT_390x390x190.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_PART_LAYOUT_390x390x190.getLayoutTemplate(), templateParam0, templateParam1, templateParam2);
                    break;
                case 6:
                    layoutTemplate=LayoutEnum.THREE_COLUMN_LAYOUT_254x717x239.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.THREE_COLUMN_LAYOUT_254x717x239.getLayoutTemplate(), templateParam0, templateParam1, templateParam2);
                    break;
                case 7:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_254x956.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_254x956.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 8:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_272x718.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_272x718.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 9:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_215x765.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_215x765.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 10:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_330x650.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_330x650.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 11:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_650x330.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_650x330.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 12:
                    layoutTemplate=LayoutEnum.LEFT_RIGHT_PART_LAYOUT_490x490.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_RIGHT_PART_LAYOUT_490x490.getLayoutTemplate(), templateParam0, templateParam1);
                    break;
                case 13:
                    layoutTemplate=LayoutEnum.LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323.getLayoutTemplate(), templateParam0, templateParam1, templateParam2);
                    break;
                case 14:
                    layoutTemplate=LayoutEnum.WITHOUT_COLUMN_LAYOUT_99999.getLayoutTemplate(moduleTemplateList);
//                    layoutTemplate = String.format(LayoutEnum.WITHOUT_COLUMN_LAYOUT_99999.getLayoutTemplate(), templateParam0);
                    break;
            }
        }
        return layoutTemplate;
    }
}
