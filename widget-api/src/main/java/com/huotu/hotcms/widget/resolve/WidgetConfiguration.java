/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.resolve.impl.CSSSerializer;
import org.thymeleaf.DialectConfiguration;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.context.IEngineContextFactory;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.ElementDefinitions;
import org.thymeleaf.engine.TemplateManager;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.linkbuilder.ILinkBuilder;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.postprocessor.IPostProcessor;
import org.thymeleaf.preprocessor.IPreProcessor;
import org.thymeleaf.processor.cdatasection.ICDATASectionProcessor;
import org.thymeleaf.processor.comment.ICommentProcessor;
import org.thymeleaf.processor.doctype.IDocTypeProcessor;
import org.thymeleaf.processor.element.IElementProcessor;
import org.thymeleaf.processor.processinginstruction.IProcessingInstructionProcessor;
import org.thymeleaf.processor.templateboundaries.ITemplateBoundariesProcessor;
import org.thymeleaf.processor.text.ITextProcessor;
import org.thymeleaf.processor.xmldeclaration.IXMLDeclarationProcessor;
import org.thymeleaf.standard.serializer.StandardSerializers;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateparser.markup.decoupled.IDecoupledTemplateLogicResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author CJ
 */
public class WidgetConfiguration implements IEngineConfiguration {

    private final IEngineConfiguration configuration;
    private final Widget widget;
    private final WidgetStyle style;
    private CSSSerializer cssSerializer = new CSSSerializer();

    WidgetConfiguration(IEngineConfiguration configuration, Widget widget, WidgetStyle style) {
        this.configuration = configuration;
        this.widget = widget;
        this.style = style;
    }

    @Override
    public Set<ITemplateResolver> getTemplateResolvers() {
        return configuration.getTemplateResolvers();
    }

    @Override
    public Set<IMessageResolver> getMessageResolvers() {
        return configuration.getMessageResolvers();
    }

    @Override
    public Set<ILinkBuilder> getLinkBuilders() {
        Set<ILinkBuilder> linkBuilders = new HashSet<>();
        linkBuilders.add(new WidgetILinkBuilder());
        return linkBuilders;
    }

    @Override
    public ICacheManager getCacheManager() {
        return configuration.getCacheManager();
    }

    @Override
    public IEngineContextFactory getEngineContextFactory() {
        return configuration.getEngineContextFactory();
    }

    @Override
    public IDecoupledTemplateLogicResolver getDecoupledTemplateLogicResolver() {
        return configuration.getDecoupledTemplateLogicResolver();
    }

    @Override
    public Set<DialectConfiguration> getDialectConfigurations() {
        return configuration.getDialectConfigurations();
    }

    @Override
    public Set<IDialect> getDialects() {
        return configuration.getDialects();
    }

    @Override
    public boolean isStandardDialectPresent() {
        return configuration.isStandardDialectPresent();
    }

    @Override
    public String getStandardDialectPrefix() {
        return configuration.getStandardDialectPrefix();
    }

    @Override
    public ElementDefinitions getElementDefinitions() {
        return configuration.getElementDefinitions();
    }

    @Override
    public AttributeDefinitions getAttributeDefinitions() {
        return configuration.getAttributeDefinitions();
    }

    @Override
    public Set<ITemplateBoundariesProcessor> getTemplateBoundariesProcessors(TemplateMode templateMode) {
        return configuration.getTemplateBoundariesProcessors(templateMode);
    }

    @Override
    public Set<ICDATASectionProcessor> getCDATASectionProcessors(TemplateMode templateMode) {
        return configuration.getCDATASectionProcessors(templateMode);
    }

    @Override
    public Set<ICommentProcessor> getCommentProcessors(TemplateMode templateMode) {
        return configuration.getCommentProcessors(templateMode);
    }

    @Override
    public Set<IDocTypeProcessor> getDocTypeProcessors(TemplateMode templateMode) {
        return configuration.getDocTypeProcessors(templateMode);
    }

    @Override
    public Set<IElementProcessor> getElementProcessors(TemplateMode templateMode) {
        return configuration.getElementProcessors(templateMode);
    }

    @Override
    public Set<ITextProcessor> getTextProcessors(TemplateMode templateMode) {
        return configuration.getTextProcessors(templateMode);
    }

    @Override
    public Set<IProcessingInstructionProcessor> getProcessingInstructionProcessors(TemplateMode templateMode) {
        return configuration.getProcessingInstructionProcessors(templateMode);
    }

    @Override
    public Set<IXMLDeclarationProcessor> getXMLDeclarationProcessors(TemplateMode templateMode) {
        return configuration.getXMLDeclarationProcessors(templateMode);
    }

    @Override
    public Set<IPreProcessor> getPreProcessors(TemplateMode templateMode) {
        return configuration.getPreProcessors(templateMode);
    }

    @Override
    public Set<IPostProcessor> getPostProcessors(TemplateMode templateMode) {
        return configuration.getPostProcessors(templateMode);
    }

    @Override
    public Map<String, Object> getExecutionAttributes() {
        return new Map<String, Object>() {
            Map<String, Object> src = configuration.getExecutionAttributes();

            @Override
            public Object get(Object key) {
                if (StandardSerializers.STANDARD_CSS_SERIALIZER_ATTRIBUTE_NAME.equals(key))
                    return cssSerializer;
                return src.get(key);
            }

            @Override
            public int size() {
                return src.size();
            }

            @Override
            public boolean isEmpty() {
                return src.isEmpty();
            }

            @Override
            public boolean containsKey(Object key) {
                return src.containsKey(key);
            }

            @Override
            public boolean containsValue(Object value) {
                return src.containsValue(value);
            }

            @Override
            public Object put(String key, Object value) {
                return src.put(key, value);
            }

            @Override
            public Object remove(Object key) {
                return src.remove(key);
            }

            @Override
            public void putAll(Map<? extends String, ?> m) {
                src.putAll(m);
            }

            @Override
            public void clear() {
                src.clear();
            }

            @Override
            public Set<String> keySet() {
                return src.keySet();
            }

            @Override
            public Collection<Object> values() {
                return src.values();
            }

            @Override
            public Set<Entry<String, Object>> entrySet() {
                return src.entrySet();
            }

            @Override
            public boolean equals(Object o) {
                return src.equals(o);
            }

            @Override
            public int hashCode() {
                return src.hashCode();
            }

            @Override
            public Object getOrDefault(Object key, Object defaultValue) {
                return src.getOrDefault(key, defaultValue);
            }

            @Override
            public void forEach(BiConsumer<? super String, ? super Object> action) {
                src.forEach(action);
            }

            @Override
            public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
                src.replaceAll(function);
            }

            @Override
            public Object putIfAbsent(String key, Object value) {
                return src.putIfAbsent(key, value);
            }

            @Override
            public boolean remove(Object key, Object value) {
                return src.remove(key, value);
            }

            @Override
            public boolean replace(String key, Object oldValue, Object newValue) {
                return src.replace(key, oldValue, newValue);
            }

            @Override
            public Object replace(String key, Object value) {
                return src.replace(key, value);
            }

            @Override
            public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
                return src.computeIfAbsent(key, mappingFunction);
            }

            @Override
            public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
                return src.computeIfPresent(key, remappingFunction);
            }

            @Override
            public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
                return src.compute(key, remappingFunction);
            }

            @Override
            public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
                return src.merge(key, value, remappingFunction);
            }
        };
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return configuration.getExpressionObjectFactory();
    }

    @Override
    public TemplateManager getTemplateManager() {
        return configuration.getTemplateManager();
    }

    @Override
    public IModelFactory getModelFactory(TemplateMode templateMode) {
        return configuration.getModelFactory(templateMode);
    }

    public WidgetStyle getStyle() {
        return style;
    }

    public Widget getWidget() {
        return widget;
    }


}
