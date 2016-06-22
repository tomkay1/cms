/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve.thymeleaf;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.EngineEventUtils;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;
import org.thymeleaf.templatemode.TemplateMode;

import java.net.URISyntaxException;

/**
 * @author CJ
 */
@Component
public class SrcProcessor extends AbstractAttributeTagProcessor implements IElementTagProcessor
        , IAttributeDefinitionsAware, WidgetProcessor {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private WidgetResolveService widgetResolveService;
    private AttributeDefinitions attributeDefinitions;


    public SrcProcessor() {
        super(TemplateMode.HTML, WidgetDialect.Prefix, null, true, "src", true, 10000, false);
//        super(TemplateMode.HTML, WidgetDialect.Prefix, "src", 10000, true);
    }


    @Override
    public void setAttributeDefinitions(AttributeDefinitions attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        String resourceName;
        try {
            final IStandardExpression expression = EngineEventUtils.computeAttributeExpression(context, tag
                    , attributeName, attributeValue);
            if (expression != null && expression instanceof FragmentExpression) {
                // This is merely a FragmentExpression (not complex, not combined with anything), so we can apply a shortcut
                // so that we don't require a "null" result for this expression if the template does not exist. That will
                // save a call to resource.exists() which might be costly.

                final FragmentExpression.ExecutedFragmentExpression executedFragmentExpression =
                        FragmentExpression.createExecutedFragmentExpression(context, (FragmentExpression) expression
                                , StandardExpressionExecutionContext.NORMAL);

                resourceName =
                        FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true)
                                .toString();

            } else if (expression != null) {
                resourceName = expression.execute(context).toString();
            } else
                resourceName = attributeValue;
        } catch (Exception ex) {
            resourceName = attributeValue;
        }

        Widget widget = (Widget) context.getVariable("widget");
        try {
            structureHandler.replaceAttribute(attributeName,attributeName.getAttributeName()
                    , widgetResolveService.resourceURI(widget, attributeValue).toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
