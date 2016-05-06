package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.common.BasicPageType;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.LayoutEnum;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.model.widget.WidgetLayout;
import com.huotu.hotcms.service.model.widget.WidgetModule;
import com.huotu.hotcms.service.model.widget.WidgetPage;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import com.huotu.hotcms.service.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * <p>
 * 页面资源服务
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
    private PageResolveService pageResolveService;

    @Autowired
    private WidgetResolveService widgetResolveService;

    @Autowired
    private WidgetResourceService widgetResourceService;

    private TemplateEngine templateEngine=new TemplateEngine();

    /**
     * <p>
     *     获得页面浏览视图
     * </p>
     * @param widgetPage 页面对象
     * @param isEdit 是否是编辑视图
     * @param site  站点信息对象
     * @return 返回页面视图模版
     * */
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


    private String getWidgetModuleTemplateByWidgetModule(WidgetModule widgetModule,Boolean isEdit,Site site) throws Exception {
        String moduleTemplate = "";
        if (widgetModule != null) {
            List<WidgetBase> widgetBases = widgetModule.getWidget();
            for (WidgetBase widgetBase : widgetBases) {
                if (widgetBase != null) {
                    widgetBase.setEdit(isEdit);
                    if(widgetBase.getGuid()==null){
                        widgetBase.setGuid(UUID.randomUUID().toString());
                    }
                    String template=widgetResourceService.getWidgetTemplateResolveByWidgetBase(widgetBase,site);
                    moduleTemplate +=template;
                }
            }
        }
        return moduleTemplate;
    }

    /**
     * 解析模块列表下面的组件Html 模版
     */
    private List<String> getWidgetModuleTemplateByWidgetModuleList(List<WidgetModule> widgetModules,Boolean isEdit,Site site) throws Exception {
        List<String> moduleTemplateList = new ArrayList<>();
        if (widgetModules != null) {
            for (WidgetModule widgetModule : widgetModules) {
                if(widgetModule.getWidget()!=null) {
                    String moduleTemplate = "";
                    moduleTemplate += getWidgetModuleTemplateByWidgetModule(widgetModule, isEdit,site);
                    moduleTemplateList.add(moduleTemplate);
                }else{
                    String moduleTemplate = "";
                    moduleTemplateList.add(moduleTemplate);
                }
            }
        }
        return moduleTemplateList;
    }

    private String getWidgetLayoutTemplateByWidgetLayout(WidgetLayout widgetLayout,Boolean isEdit,Site site) throws Exception {
        String layoutTemplate = "";
        if (widgetLayout != null) {
            List<String> moduleTemplateList = getWidgetModuleTemplateByWidgetModuleList(widgetLayout.getModule(),isEdit,site);
            LayoutEnum layoutEnum = EnumUtils.valueOf(LayoutEnum.class, widgetLayout.getLayoutType());
            layoutTemplate=layoutEnum.getLayoutTemplate(moduleTemplateList,isEdit,widgetLayout.getLayoutId());
        }
        return layoutTemplate;
    }

//    /**
//     * <p>
//     *     根据站点获得头部模版信息对象
//     * </p>
//     * @param site 站点信息对象
//     * @return 返回头部浏览视图,已经解析后的html视图
//     * */
//    public String getHeaderTemplateBySite(Site site) throws Exception {
//        WidgetPage widgetPage=pageResolveService.getWidgetPageByConfig("head.xml", site);
//        if(widgetPage!=null){
//           return getHtmlTemplateByWidgetPage(widgetPage, false,site);
//        }
//        return null;
//    }

    /***
     *  根据站点获得公共页面模版
     *
     * @param site
     * @param basicPageType
     * @return
     * @throws Exception
     */
    public String getCommonTemplateBySite(Site site,BasicPageType basicPageType) throws Exception {
        WidgetPage widgetPage=pageResolveService.getWidgetPageByConfig(basicPageType.getValue(), site);
        if(widgetPage!=null){
            return getHtmlTemplateByWidgetPage(widgetPage, false,site);
        }
        return null;
    }

//    /**
//     * 根据站点获得底部模版信息对象
//     *
//     * @param site
//     * @return
//     * @throws Exception
//     */
//    public String getBottomTemplateBySite(Site site) throws Exception {
//        WidgetPage widgetPage=pageResolveService.getWidgetPageByConfig("bottom.xml", site);
//        if(widgetPage!=null){
//            return getHtmlTemplateByWidgetPage(widgetPage, false,site);
//        }
//        return null;
//    }

    /**
     * <P>
     *     获得头部浏览视图
     * </P>
     * @param resources 资源
     * @param  widgetPage
     * @param site
     * @return String
     * */
    public String getHeaderTemplate(String resources,WidgetPage widgetPage,Site site){
        try{
            Context context=new Context(Locale.CHINA, ReflectionUtil.getFieldList(widgetPage));
            StringWriter writer = new StringWriter();
            context=widgetResolveService.setVariable(context,site,widgetPage);
            templateEngine.process(resources,context,writer);
            return writer.toString();
        }catch (Exception ex){
            return resources;
        }
    }
}
