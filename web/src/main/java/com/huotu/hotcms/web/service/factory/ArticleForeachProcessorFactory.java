/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.thymeleaf.ArticleForeachParam;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
public class ArticleForeachProcessorFactory {

    private static ArticleForeachProcessorFactory instance;

    private ArticleForeachProcessorFactory() {
    }

    public static ArticleForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleForeachProcessorFactory();
        }
        return instance;
    }

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        ArticleForeachParam articleForeachParam = null;
        try {
            articleForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag,ArticleForeachParam.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        List<Article> articles = new ArrayList<>();
//            HostService hostService = (HostService)applicationContext.getBean("hostServiceImpl");
//            Host host = hostService.getHost(request.getServerName());
//            Set<Site> sites = host.getSites();
        return articles;
    }
}
