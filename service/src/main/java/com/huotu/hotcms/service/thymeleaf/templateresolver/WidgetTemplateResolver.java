/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.templateresolver;

import com.huotu.hotcms.service.thymeleaf.templateresource.WidgetTemplateResource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.StringUtils;
import org.thymeleaf.util.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 组件模板资源解析器
 * Created by cwb on 2016/3/16.
 */
public class WidgetTemplateResolver extends AbstractTemplateResolver {

    public static final TemplateMode DEFAULT_TEMPLATE_MODE = TemplateMode.XML;
    private ApplicationContext applicationContext = null;
    private TemplateMode templateMode = DEFAULT_TEMPLATE_MODE;
    private String prefix = null;
    private String suffix = null;
    private String characterEncoding = null;
    private final HashMap<String,String> templateAliases = new HashMap<String, String>(8);

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    public final void setTemplateMode(final TemplateMode templateMode) {
        Validate.notNull(templateMode, "Cannot set a null template mode value");
        // We re-parse the specified template mode so that we make sure we get rid of deprecated values
        this.templateMode = TemplateMode.parse(templateMode.toString());
    }
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public final Map<String, String> getTemplateAliases() {
        return Collections.unmodifiableMap(this.templateAliases);
    }

    public final void setTemplateAliases(final Map<String,String> templateAliases) {
        if (templateAliases != null) {
            this.templateAliases.putAll(templateAliases);
        }
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        final String resourceName =
                computeResourceName(configuration, ownerTemplate, template, this.prefix, this.suffix, this.templateAliases, templateResolutionAttributes);
        return new WidgetTemplateResource(this.applicationContext, resourceName, characterEncoding);
    }

    @Override
    protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        return TemplateMode.XML;
    }

    @Override
    protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        return NonCacheableCacheEntryValidity.INSTANCE;
    }

    protected String computeResourceName(
            final IEngineConfiguration configuration, final String ownerTemplate, final String template,
            final String prefix, final String suffix, final Map<String, String> templateAliases,
            final Map<String, Object> templateResolutionAttributes) {

        Validate.notNull(template, "Template name cannot be null");

        String unaliasedName = templateAliases.get(template);
        if (unaliasedName == null) {
            unaliasedName = template;
        }

        final boolean hasPrefix = !StringUtils.isEmptyOrWhitespace(prefix);
        final boolean hasSuffix = !StringUtils.isEmptyOrWhitespace(suffix);

        if (!hasPrefix && !hasSuffix){
            return unaliasedName;
        }

        if (!hasPrefix) { // hasSuffix
            return unaliasedName + suffix;
        }

        if (!hasSuffix) { // hasPrefix
            return prefix + unaliasedName;
        }

        // hasPrefix && hasSuffix
        return prefix + unaliasedName + suffix;

    }
}
