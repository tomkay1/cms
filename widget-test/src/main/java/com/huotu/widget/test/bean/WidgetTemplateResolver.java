/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.widget.Widget;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.spring4.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

/**
 * 将控件内模板读取出来
 *
 * @author CJ
 */
public class WidgetTemplateResolver extends AbstractTemplateResolver {

    /**
     * 编辑器的模板名称
     *
     * @see Widget#editorTemplate()
     */
    public static final String EDITOR = "EDITOR";
    private final Widget widget;

    public WidgetTemplateResolver(Widget widget) {
        this.widget = widget;
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate
            , String template, Map<String, Object> templateResolutionAttributes) {
        switch (template) {
            case EDITOR:
                return new SpringResourceTemplateResource(widget.editorTemplate(), "UTF-8");
        }
        // SpringResourceTemplateResource
        return null;
    }

    @Override
    protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template
            , Map<String, Object> templateResolutionAttributes) {
        return TemplateMode.HTML;
    }

    @Override
    protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate
            , String template, Map<String, Object> templateResolutionAttributes) {
        return NonCacheableCacheEntryValidity.INSTANCE;
    }
}
