/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.common.SysConstant;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.util.PageUtils;
import com.huotu.hotcms.service.widget.model.Goods;
import com.huotu.hotcms.service.widget.model.GoodsModel;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.model.JsonModel;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分页组件处理器
 * Created by cwb on 2016/3/17.
 */
public class GoodsPageableTagProcessor extends AbstractAttributeTagProcessor {

    public static final String ATTR_NAME = "page";
    public static final int PRECEDENCE = 1300;

    private static Log log = LogFactory.getLog(GoodsPageableTagProcessor.class);

    public GoodsPageableTagProcessor(IProcessorDialect dialect, String dialectPrefix) {
        super(dialect, TemplateMode.XML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        final Object iteratedValue;
        iteratedValue = invokeGoodsPageableService(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }

    private Object invokeGoodsPageableService(IProcessableElementTag tag, ITemplateContext context) {
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        GoodsService goodsService = (GoodsService)applicationContext.getBean("goodsServiceImpl");
        int customerId = ((Site)VariableExpression.getVariable(context, "site")).getCustomerId();
        JsonModel goodsPage = null;
        try {
            GoodsSearcher goodsSearcher = DialectAttributeFactory.getInstance().getForeachParam(tag, GoodsSearcher.class);
            goodsPage = goodsService.searchGoods(customerId,goodsSearcher);
            putPageAttrsIntoModel(context,goodsPage);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return goodsPage.get_embedded().getGoodses();
    }

    private void putPageAttrsIntoModel(ITemplateContext context,JsonModel goodsPage) {
        //分页标签处理
        RequestModel requestModel = (RequestModel)VariableExpression.getVariable(context,"request");
        int pageNo = goodsPage.getPage().getNumber()+1;
        int totalPages = goodsPage.getPage().getTotalPages();
        int pageBtnNum = totalPages > SysConstant.DEFAULT_PAGE_BUTTON_NUM ? SysConstant.DEFAULT_PAGE_BUTTON_NUM : totalPages;
        int startPageNo = PageUtils.calculateStartPageNo(pageNo, pageBtnNum, totalPages);
        List<Integer> pageNos = new ArrayList<>();
        for (int i = 1; i <= pageBtnNum; i++) {
            pageNos.add(startPageNo);
            startPageNo++;
        }
        requestModel.setCurrentPage(pageNo);
        requestModel.setTotalPages(totalPages);
        requestModel.setTotalRecords(goodsPage.getPage().getTotalElements());
        requestModel.setHasPrevPage(pageNo > 1);
        requestModel.setHasNextPage(pageNo < totalPages);
        requestModel.setPageNos(pageNos);
    }
}
