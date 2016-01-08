/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.model.thymeleaf.ArticleForeachParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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

    private static Log log = LogFactory.getLog(ArticleForeachProcessorFactory.class);

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
        Page<Article> articles = null;
        try {
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            ArticleForeachParam articleForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, ArticleForeachParam.class);
            if(StringUtils.isEmpty(articleForeachParam.getCategoryid())) {
                Route route = (Route)VariableExpression.getVariable(context,"route");
                if(route.getRouteType()==RouteType.ARTICLEDETILE) {
                    throw new Exception("路由规则错误");
                }
            }
            ArticleService articleService = (ArticleService)applicationContext.getBean("articleServiceImpl");
            articles = articleService.getArticleList(articleForeachParam);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return articles;
    }
}
