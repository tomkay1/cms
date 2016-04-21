/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.PreviousProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 * 自定义thymeleaf 方言(上一条输出语法方言)
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class PreviousProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 200;

    public static final String ATTR_NAME = "previous";
    private final PreviousProcessorService previousProcessorService;
    private final String dialectPrefix;

    public PreviousProcessor(final IProcessorDialect dialect, final String dialectPrefix
            , PreviousProcessorService previousProcessorService) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.previousProcessorService = previousProcessorService;
//        this.previousProcessorService.setDialectPrefix(dialectPrefix);
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler){
        final Object iteratedValue;
        iteratedValue = previousProcessorService.resolveDataByAttr(dialectPrefix, tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
