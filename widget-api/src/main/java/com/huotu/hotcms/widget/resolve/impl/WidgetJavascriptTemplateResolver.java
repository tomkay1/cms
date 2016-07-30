/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.impl;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.spring4.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.util.Map;

/**
 * 一款专门用于解析widget Javascript的模板处理器
 *
 * @author CJ
 * @see Widget#widgetJs()
 */
@Component
public class WidgetJavascriptTemplateResolver extends AbstractTemplateResolver {

    @Autowired
    private WidgetLocateService widgetLocateService;
    @Autowired
    private ResourceService resourceService;

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate
            , String template, Map<String, Object> templateResolutionAttributes) {
        int length = template.length();
        String identifier = template.substring(0, length - ".WidgetJS".length());
        Widget widget = widgetLocateService.findWidget(identifier).getWidget();

        //不可能为null
        String jsPath = Widget.widgetJsResourcePath(widget);

        Resource resource = resourceService.getResource(jsPath);
        if (resource == null || !resource.exists()) {
            return new StringTemplateResource("");
        }

        return new SpringResourceTemplateResource(resource, "UTF-8");
    }

    @Override
    protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template
            , Map<String, Object> templateResolutionAttributes) {
        return TemplateMode.JAVASCRIPT;
    }

    @Override
    protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        return NonCacheableCacheEntryValidity.INSTANCE;
    }
}
