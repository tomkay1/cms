/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.SrcProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public class SrcProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "src";

    private final SrcProcessorService srcProcessorService;
    private final String dialectPrefix;

    public SrcProcessor(final IProcessorDialect dialect, final String dialectPrefix, SrcProcessorService srcProcessorService) {
//        super(dialect, TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        super(TemplateMode.HTML, dialectPrefix,  ATTR_NAME, PRECEDENCE, true);
        this.srcProcessorService = srcProcessorService;
//        this.srcProcessorService.setDialectPrefix(dialectPrefix);
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             Object expressionResult,
                             IElementTagStructureHandler structureHandler) {
        String newAttributeValue=null;
        Object srcObject = this.srcProcessorService.resolveSrcData(dialectPrefix, attributeValue, context);
        newAttributeValue=srcObject!=null?srcObject.toString():"";

        newAttributeValue= HtmlEscape.escapeHtml4Xml(newAttributeValue == null ? "" : newAttributeValue.toString());

        // Let RequestDataValueProcessor modify the attribute value if needed
        newAttributeValue = RequestDataValueProcessorUtils.processUrl(context, newAttributeValue);
        // Set the real, non prefixed attribute

//        tag.getAttributes().replaceAttribute(attributeName, ATTR_NAME, (newAttributeValue == null? "" : newAttributeValue));
        StandardProcessorUtils.replaceAttribute(
                structureHandler, attributeName, null, null, (newAttributeValue == null ? "" : newAttributeValue));
    }
}
