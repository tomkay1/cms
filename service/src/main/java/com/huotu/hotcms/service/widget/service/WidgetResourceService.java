/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.widget.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.widget.WidgetBase;
import com.huotu.hotcms.service.service.RedisService;
import com.huotu.hotcms.service.util.HttpUtils;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * <p>
 *     组件资源服务
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Component
public class WidgetResourceService {
    private static final Log log = LogFactory.getLog(WidgetResolveService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private WidgetResolveService widgetResolveService;

    /**
     * <p>
     *  获得组件浏览视图
     * </p>
     * @param widgetBase 页面组件信息
     * */
    public String getWidgetTemplateByWidgetBase(WidgetBase widgetBase) throws Exception {
        if (redisService.isContent())
            return isWidgetTemplateCached(widgetBase) ? getCachedWidgetTemplate(widgetBase) : getWidgetTemplateFromFile(widgetBase);
        else
            return getWidgetTemplateFromFile(widgetBase);
    }

    /**
     * <p>
     *     从缓存中获得组件模版
     * </p>
     * */
    private String getCachedWidgetTemplate(WidgetBase widgetBase) {
        return redisService.findByWidgetId(widgetBase.getId());
    }

    private boolean isWidgetTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetExists(widgetBase.getId());
    }

    /**
     * <p>
     *     根据控件主体信息获得控件主体模版
     * </p>
     * @param widgetBase 控件主体对象
     * */
    private String getWidgetTemplateFromFile(WidgetBase widgetBase) throws Exception{
        URL url = resourceService.getResource(widgetBase.getWidgetUri()).httpUrl();
        String widgetTemplate = HttpUtils.getHtmlByUrl(url);
        if(redisService.isContent()) {
            redisService.saveWidget(widgetBase.getId(), widgetTemplate);
        }
        return widgetTemplate;
    }

    public String getWidgetTemplateResolveByWidgetBase(WidgetBase widgetBase,Site site) throws Exception {
        String widgetHtml=getWidgetTemplateByWidgetBase(widgetBase);
        widgetHtml=widgetResolveService.widgetBriefView(widgetHtml,widgetBase,site);
        return widgetHtml;
    }

    /**
     * <p>
     *     获得控件主体编辑视图,如果缓存存在则取缓存里面的信息,反之取物理资源里面的模版信息，并且更新缓存信息
     * </p>
     * @param widgetBase 控件主体对象
     * @return 返回控件主体编辑视图
     * */
    public String getWidgetEditTemplate(WidgetBase widgetBase) {
        if (redisService.isContent())
            return isWidgetEditTemplateCached(widgetBase) ? getCachedWidgetEditTemplate(widgetBase) : getWidgetEditTemplateFromFile(widgetBase);
        else
            return getWidgetEditTemplateFromFile(widgetBase);
    }

    /**
     * <p>
     *     根据控件主体信息来判断改编辑视图是否存在缓存信息
     * </p>
     * @param widgetBase 控件主体信息对象
     * @return 存在则返回true,否则返回false
     * */
    private boolean isWidgetEditTemplateCached(WidgetBase widgetBase) {
        return redisService.isWidgetEditExists(widgetBase.getId());
    }

    private String getCachedWidgetEditTemplate(WidgetBase widgetBase) {
        return redisService.findByWidgetEditId(widgetBase.getId());
    }

    private String getWidgetEditTemplateFromFile(WidgetBase widgetBase) {
        try {
            URL url = resourceService.getResource(widgetBase.getWidgetEditUri()).httpUrl();
            String widgetTemplate = HttpUtils.getHtmlByUrl(url);
            if(redisService.isContent()){
                redisService.saveWidgetEdit(widgetBase.getId(), widgetTemplate);
            }
            return widgetTemplate;
        } catch (Exception ex) {
            log.error("获得控件主体编辑视图失败");
            throw new ExceptionInInitializerError("获得控件主体编辑视图异常");
        }
    }
}
