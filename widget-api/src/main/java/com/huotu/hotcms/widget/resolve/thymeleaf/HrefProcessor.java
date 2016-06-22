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
import com.huotu.hotcms.widget.support.ExpressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by lhx on 2016/6/20.
 */
@Component
public class HrefProcessor extends AbstractAttributeTagProcessor implements IElementTagProcessor
        , IAttributeDefinitionsAware, WidgetProcessor {
    @Autowired
    WidgetResolveService widgetResolveService;
    private AttributeDefinitions attributeDefinitions;

    public HrefProcessor() {
        super(TemplateMode.HTML, WidgetDialect.Prefix, null, true, "href", true, 10000, false);
    }


    @Override
    public void setAttributeDefinitions(AttributeDefinitions attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        String resourceName = (String) ExpressionUtils.ParseInputElseInput(context, attributeValue);

        Widget widget = (Widget) context.getVariable("widget");
        try {
            structureHandler.replaceAttribute(attributeName, attributeName.getAttributeName()
                    , widgetResolveService.resourceURI(widget, resourceName).toString());
        } catch (IOException | URISyntaxException e) {
            structureHandler.replaceAttribute(attributeName, attributeName.getAttributeName()
                    , attributeValue);
        }
    }

}
