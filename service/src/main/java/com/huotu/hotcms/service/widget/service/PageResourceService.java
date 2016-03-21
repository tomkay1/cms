package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.model.widget.*;
import com.huotu.hotcms.service.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
public class PageResourceService {
    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private PageResolveService pageResolveService;

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage){
        String htmlTemplate="";
        if(widgetPage!=null){
           List<WidgetLayout> widgetLayouts= widgetPage.getLayout();
           if(widgetLayouts!=null){
               for(WidgetLayout widgetLayout:widgetLayouts){
                   if(widgetLayout!=null) {
                       htmlTemplate+=getWidgetLayoutTemplateByWidgetLayout(widgetLayout);
                   }
               }
           }
        }
        return htmlTemplate;
    }

    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase){
        String widgetTemplate="";
        if(widgetBase!=null){
            try {
                URL url=resourceServer.getResource(widgetBase.getWidgetUri()).toURL();
                List<WidgetProperty> widgetProperties= widgetBase.getProperty();
                widgetTemplate=HttpUtils.getHtmlByUrl(url);
                if(widgetProperties!=null){
                   for(WidgetProperty widgetProperty:widgetProperties){
                       if(widgetProperty!=null){
                           widgetTemplate=widgetTemplate.replace("{"+widgetProperty.getName()+"}",widgetProperty.getValue());
                       }
                   }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return widgetTemplate;
    }

    public String getWidgetModuleTemplateByWidgetModule(WidgetModule widgetModule){
        String moduleTemplate="";
        if(widgetModule!=null){
            List<WidgetBase> widgetBases=widgetModule.getWidget();
            for(WidgetBase widgetBase:widgetBases){
                moduleTemplate+=getWidgetTemplateByWidgetBase(widgetBase);
            }
        }
        return moduleTemplate;
    }

    /**
     * 解析模块列表下面的组件Html 模版
     * */
    public List<String> getWidgetModuleTemplateByWidgetModuleList(List<WidgetModule> widgetModules){
        List<String> moduleTemplateList=new ArrayList<>();
        if(widgetModules!=null){
            for (WidgetModule widgetModule:widgetModules){
                String moduleTemplate="";
                moduleTemplate+=getWidgetModuleTemplateByWidgetModule(widgetModule);
                moduleTemplateList.add(moduleTemplate);
            }
        }
        return moduleTemplateList;
    }

    public String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout){
        String layoutTemplate="";
        if(widgetLayout!=null){
            List<String> moduleTemplateList=getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule());
            String templateParam0="";
            String templateParam1="";
            String templateParam2="";
            if(moduleTemplateList!=null) {
                try {
                    templateParam0 = moduleTemplateList.get(0);
                    templateParam1 = moduleTemplateList.get(1);
                    templateParam2 = moduleTemplateList.get(2);
                }catch (Exception ex){
                    ex.getStackTrace();
                }
            }
            switch (widgetLayout.getLayoutType()){
                case 0:
                    layoutTemplate= String.format(LayoutEnum.THREE_COLUMN_LAYOUT_190x590x190.getLayoutTemplate(),templateParam0,templateParam1,templateParam2);
                    break;
                case 1:
                    layoutTemplate= String.format(LayoutEnum.WITHOUT_COLUMN_LAYOUT_990.getLayoutTemplate(),templateParam0);
                    break;
                case 2:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_190x790.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 3:
                    layoutTemplate= String.format(LayoutEnum.RIGHT_PART_LAYOUT_190x390x390.getLayoutTemplate(),templateParam0,templateParam1,templateParam2);
                    break;
                case 4:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_790x190.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 5:
                    layoutTemplate= String.format(LayoutEnum.LEFT_PART_LAYOUT_390x390x190.getLayoutTemplate(),templateParam0,templateParam1,templateParam2);
                    break;
                case 6:
                    layoutTemplate= String.format(LayoutEnum.THREE_COLUMN_LAYOUT_254x717x239.getLayoutTemplate(),templateParam0,templateParam1,templateParam2);
                    break;
                case 7:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_254x956.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 8:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_272x718.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 9:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_215x765.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 10:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_330x650.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 11:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_COLUMN_LAYOUT_650x330.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 12:
                    layoutTemplate= String.format(LayoutEnum.LEFT_RIGHT_PART_LAYOUT_490x490.getLayoutTemplate(),templateParam0,templateParam1);
                    break;
                case 13:
                    layoutTemplate= String.format(LayoutEnum.LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323.getLayoutTemplate(),templateParam0,templateParam1,templateParam2);
                    break;
                case 14:
                    layoutTemplate= String.format(LayoutEnum.WITHOUT_COLUMN_LAYOUT_99999.getLayoutTemplate(),templateParam0);
                    break;
            }
        }
        return layoutTemplate;
    }
}
