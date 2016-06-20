/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf.process;

import com.huotu.hotcms.widget.Widget;
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
 * @author CJ
 */
@Component
public class SrcProcessor extends AbstractStandardExpressionAttributeTagProcessor implements IElementTagProcessor
        , IAttributeDefinitionsAware {

    private AttributeDefinitions attributeDefinitions;

    public SrcProcessor() {
        super(TemplateMode.HTML, "w", "src", 10000, true);
    }


    @Override
    public void setAttributeDefinitions(AttributeDefinitions attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }

    /**
     * @param context
     * @param tag
     * @param attributeName
     * @param attributeValue   原值 针对 {@link Widget#publicResources()}  的key
     * @param expressionResult
     * @param structureHandler
     */
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, Object expressionResult, IElementTagStructureHandler structureHandler) {
        // attributeValue 针对
    }
}
