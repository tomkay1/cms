/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.service.thymeleaf;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Created by lhx on 2016/6/20.
 */
@Component
public class HrefProcessor extends AbstractStandardExpressionAttributeTagProcessor implements IElementTagProcessor
        , IAttributeDefinitionsAware,WidgetProcessor {
    @Autowired
    WidgetService widgetService;
    private AttributeDefinitions attributeDefinitions;

    public HrefProcessor() {
        super(TemplateMode.HTML, WidgetDialect.Prefix, "href", 10000, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, Object expressionResult, IElementTagStructureHandler structureHandler) {
        Widget widget = (Widget) context.getVariable("widget");
        structureHandler.replaceAttribute(attributeName,attributeName.getAttributeName()
                ,widgetService.resourceURI(widget,attributeValue).toString());
    }

    @Override
    public void setAttributeDefinitions(AttributeDefinitions attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }
}
