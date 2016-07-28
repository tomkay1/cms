/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.NextProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 * 自定义thymeleaf 方言(下一条输出语法方言)
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class NextProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 200;

    public static final String ATTR_NAME = "next";
    private final String dialectPrefix;
    private final NextProcessorService nextProcessorService;

    public NextProcessor(final IProcessorDialect dialect, final String dialectPrefix, NextProcessorService nextProcessorService) {
//        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.nextProcessorService = nextProcessorService;
        this.dialectPrefix = dialectPrefix;
//        this.nextProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = nextProcessorService.resolveDataByAttr(dialectPrefix, tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
