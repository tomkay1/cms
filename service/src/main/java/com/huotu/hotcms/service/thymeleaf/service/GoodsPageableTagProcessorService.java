/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.service.factory.GoodsPageableTagProcessor;
import com.huotu.hotcms.service.widget.model.GoodsPage;
import com.huotu.hotcms.service.widget.model.GoodsSearcher;
import com.huotu.hotcms.service.widget.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/4/21.
 */
@Component
public class GoodsPageableTagProcessorService {
    private final int DEFAULT_PAGE_NUMBER = 3;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsPageableTagProcessor goodsPageableTagProcessor;
    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object invokeGoodsPageableService(IProcessableElementTag tag, ITemplateContext context) {
        Site site=(Site) context.getVariable("site");
        int customerId = site.getOwner().getCustomerId();
        GoodsPage goodsPage = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            GoodsSearcher goodsSearcher= (GoodsSearcher) goodsPageableTagProcessor.process(tag, request);
            if(goodsSearcher.getPageNumber()==null){
                goodsSearcher.setPageNumber(DEFAULT_PAGE_NUMBER);
            }
            goodsSearcher.init(goodsSearcher);
            goodsPage = goodsService.searchGoods(request, customerId, goodsSearcher);
            dialectAttributeFactory.setPageList(context,goodsPage,goodsSearcher.getPageNumber());
        } catch (Exception e) {

            e.printStackTrace();
        }
        return goodsPage == null ? null : goodsPage.getGoodses();
    }

}
