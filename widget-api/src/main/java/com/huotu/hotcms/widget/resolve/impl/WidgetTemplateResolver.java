/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.impl;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.resolve.WidgetConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.spring4.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 将控件内模板读取出来
 *
 * @author CJ
 */
@Component
public class WidgetTemplateResolver extends AbstractTemplateResolver {

    /**
     * 编辑器的模板名称
     *
     * @see Widget#editorTemplate()
     */
    public static final String EDITOR = "EDITOR";
    public static final String PREVIEW = "PREVIEW";
    public static final String BROWSE = "BROWSE";
    public static final String CSS = "CSS";
    public static final String JAVASCRIPT = "JAVASCRIPT";


    @Autowired
    ApplicationContext applicationContext;


    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate
            , String template, Map<String, Object> templateResolutionAttributes) {
        switch (template) {
            case EDITOR:
            case CSS:
            case PREVIEW:
            case BROWSE:
            case JAVASCRIPT:
                break;
            default:
                return null;
        }
        WidgetConfiguration widgetConfiguration = CMSContext.RequestContext()
                .getWidgetConfigurationStack().pop();
        Widget widget = widgetConfiguration.getWidget();
        WidgetStyle style = widgetConfiguration.getStyle();

        switch (template) {
            case EDITOR:
                return new SpringResourceTemplateResource(widget.editorTemplate(), "UTF-8");
            case CSS:
                Resource resource = widget.widgetDependencyContent(Widget.CSS);
                if (resource == null || !resource.exists()) {
                    return new StringTemplateResource("");
                }
                return new SpringResourceTemplateResource(resource, "UTF-8");
            case PREVIEW:
                return new SpringResourceTemplateResource(style.previewTemplate() != null
                        && style.previewTemplate().exists() ? style.previewTemplate()
                        : style.browseTemplate(), "UTF-8");
            case BROWSE:
                return new SpringResourceTemplateResource(style.browseTemplate(), "UTF-8");
            case JAVASCRIPT:
                // 加也应该在这里加 缓存就不要了
                resource = widget.widgetDependencyContent(Widget.Javascript);
                if (resource == null || !resource.exists()) {
                    return new StringTemplateResource("");
                }
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try {
                    buffer.write(("CMSWidgets.pushNextWidgetIdentity('" + Widget.WidgetIdentity(widget) + "');\n")
                            .getBytes());
                    try (InputStream stream = resource.getInputStream()) {
                        StreamUtils.copy(stream, buffer);
                    }
                    return new StringTemplateResource(buffer.toString("UTF-8"));
                } catch (IOException ex) {
                    throw new Error("Mem Error", ex);
                }

        }
        return null;
    }

    @Override
    protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template
            , Map<String, Object> templateResolutionAttributes) {
        switch (template) {
            case CSS:
                return TemplateMode.CSS;
            case JAVASCRIPT:
                return TemplateMode.JAVASCRIPT;
        }
        return TemplateMode.HTML;
    }

    @Override
    protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate
            , String template, Map<String, Object> templateResolutionAttributes) {
        return NonCacheableCacheEntryValidity.INSTANCE;
    }
}
