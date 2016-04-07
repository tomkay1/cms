/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.widget.model.GoodsCategory;
import com.huotu.hotcms.service.widget.service.GoodsCategoryService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

/**
 * 商品分类组件处理器
 * Created by cwb on 2016/3/17.
 */
public class GoodsCATGTagProcessor extends AbstractAttributeTagProcessor {

    public static final String ATTR_NAME = "category";
    public static final int PRECEDENCE = 1300;

    public GoodsCATGTagProcessor(IProcessorDialect dialect, String dialectPrefix) {
        super(dialect, TemplateMode.XML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = invokeGoodsCATGService(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }

    private Object invokeGoodsCATGService(IProcessableElementTag tag,ITemplateContext context){
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        GoodsCategoryService goodsCategoryService = (GoodsCategoryService)applicationContext.getBean("goodsCategoryServiceImpl");
        Site site = (Site)VariableExpression.getVariable(context,"site");
        int customerId = site.getCustomerId();
        List<GoodsCategory> categories = goodsCategoryService.getGoodsCategories(customerId);
        return categories;
    }
}
