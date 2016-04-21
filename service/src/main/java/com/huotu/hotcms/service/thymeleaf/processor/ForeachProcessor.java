/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.ForeachProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;


/**
 * Created by cwb on 2016/1/4.
 */
public class ForeachProcessor extends AbstractAttributeTagProcessor {

    public static final String ATTR_NAME = "foreach";
    public static final int PRECEDENCE = 1300;
    private final String dialectPrefix;
    private ForeachProcessorService foreachProcessorService;

//    public ForeachProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
//        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
//        this.foreachProcessorService = new ForeachProcessorService();
//        this.dialectPrefix= dialectPrefix;
////        this.foreachProcessorService.setDialectPrefix(dialectPrefix);
//    }

    public ForeachProcessor(final IProcessorDialect dialect, final String dialectPrefix
            , ForeachProcessorService foreachProcessorService) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.foreachProcessorService = foreachProcessorService;
        this.dialectPrefix = dialectPrefix;
//        this.foreachProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol
            , IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = foreachProcessorService.resolveDataByAttr(dialectPrefix, tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
