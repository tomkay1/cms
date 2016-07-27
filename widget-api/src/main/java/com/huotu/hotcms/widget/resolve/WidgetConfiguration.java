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
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateparser.markup.decoupled.IDecoupledTemplateLogicResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Map;
import java.util.Set;

/**
 * @author CJ
 */
public class WidgetConfiguration  implements IEngineConfiguration {

    private final IEngineConfiguration configuration;
    private final Widget widget;
    private final WidgetStyle style;

    public WidgetConfiguration(IEngineConfiguration configuration, Widget widget, WidgetStyle style ) {
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
        return configuration.getLinkBuilders();
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
        return configuration.getExecutionAttributes();
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
