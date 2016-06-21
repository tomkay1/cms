package com.huotu.widget.test.thymeleaf.process;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import com.huotu.widget.test.thymeleaf.WidgetProcessor;
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
    private AttributeDefinitions attributeDefinitions;

    @Autowired
    WidgetService widgetService;

    public HrefProcessor() {
        super(TemplateMode.HTML, "w", "href", 10000, true);
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
