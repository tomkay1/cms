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
import com.huotu.hotcms.service.widget.model.GoodsModel;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 热销产品组件解析器
 * Created by cwb on 2016/4/11.
 */
public class HotGoodsProcessor extends AbstractAttributeTagProcessor {

    public static final String ATTR_NAME = "hot";
    public static final int PRECEDENCE = 1300;
    private final GoodsService goodsService;

    private Log log = LogFactory.getLog(HotGoodsProcessor.class);

    public HotGoodsProcessor(IProcessorDialect dialect, String dialectPrefix, GoodsService goodsService) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.goodsService = goodsService;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = invokeHogGoodsService(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }

    private Object invokeHogGoodsService(IProcessableElementTag tag, ITemplateContext context) {
        List<GoodsModel> goodses = new ArrayList<>();
        try {
            int customerId = ((Site) VariableExpression.getVariable(context, "site")).getCustomerId();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            goodses = goodsService.getHotGoodsList(request,customerId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return goodses;
    }
}
