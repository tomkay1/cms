/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.ForeachProcessorService;
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
    private ForeachProcessorService foreachProcessorService;
    public static final int PRECEDENCE = 1300;

    public ForeachProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.foreachProcessorService = new ForeachProcessorService();
        this.foreachProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler){
        final Object iteratedValue;
        try {
            iteratedValue = foreachProcessorService.resolveDataByAttr(tag, context);
            structureHandler.iterateElement(attributeValue, null, iteratedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
