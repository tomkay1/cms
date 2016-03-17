package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.ConfigInfo;
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

    public String getHtmlTemplateByWidgetPage(WidgetPage widgetPage){
        String htmlTemplate="";
        if(widgetPage!=null){
           List<WidgetLayout> widgetLayouts= widgetPage.getLayout();
           if(widgetLayouts!=null){
               for(WidgetLayout widgetLayout:widgetLayouts){
                   if(widgetLayout!=null) {
                       List<String> moduleTemplateList=getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule());
//                       LayoutEnum.LEFT_CENTER_RIGHT_PART_LAYOUT_323x324x323.getLayoutTemplate();
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
        String moduleTemplate="";
        if(widgetModules!=null){
            for (WidgetModule widgetModule:widgetModules){
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
            switch (widgetLayout.getLayoutType()){
//                case 0:
//                    layoutTemplate= "<div>%s</div><div>%s</div><div>%s</div>";
//                    break;
//                case 1:
//                    layoutTemplate= "<div>%s</div>";
//                    break;
//                case 2:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 3:
//                    layoutTemplate= "<div>%s</div><div>%s</div><div>%s</div>";
//                    break;
//                case 4:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 5:
//                    layoutTemplate= "<div>%s</div><div>%s</div><div>%s</div>";
//                    break;
//                case 6:
//                    layoutTemplate= "<div>%s</div><div>%s</div><div>%s</div>";
//                    break;
//                case 7:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 8:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 9:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 10:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 11:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 12:
//                    layoutTemplate= "<div>%s</div><div>%s</div>";
//                    break;
//                case 13:
//                    layoutTemplate= "<div>%s</div><div>%s</div><div>%s</div>";
//                    break;
//                case 14:
//                    layoutTemplate= "<div>%s</div>";
//                    break;
            }
        }
        return layoutTemplate;
    }
}
