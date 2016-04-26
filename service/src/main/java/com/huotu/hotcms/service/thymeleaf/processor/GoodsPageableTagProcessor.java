/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.GoodsPageableTagProcessorService;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 商品分页组件处理器
 * Created by cwb on 2016/3/17.
 */
public class GoodsPageableTagProcessor extends AbstractAttributeTagProcessor {

    public static final String ATTR_NAME = "page";
    public static final int PRECEDENCE = 1300;
    private static Log log = LogFactory.getLog(GoodsPageableTagProcessor.class);
    private final GoodsPageableTagProcessorService goodsPageableTagProcessorService;

    public GoodsPageableTagProcessor(IProcessorDialect dialect, String dialectPrefix, GoodsPageableTagProcessorService goodsPageableTagProcessorService) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.goodsPageableTagProcessorService=goodsPageableTagProcessorService;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = goodsPageableTagProcessorService.invokeGoodsPageableService(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }


}
