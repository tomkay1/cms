/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;


import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     文章解析服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class ArticleResolveService {
    private static final Log log = LogFactory.getLog(ArticleResolveService.class);
    @Autowired
    private RouteResolverService routeResolverService;

    @Autowired
    private ArticleServiceImpl articleService;

    /**
     * 根据Context获得文章对象
     *
     * @param ITemplateContext
     * @return
     * */
    public Article getArticleByContent( ITemplateContext context){
        Site site = (Site) VariableExpression.getVariable(context, "site");
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        Route routeRule=routeResolverService.getRoute(site, PatternMatchUtil.getUrl(request));
        if(routeRule!=null){
            Integer articleId=PatternMatchUtil.getUrlId(PatternMatchUtil.getUrl(request),routeRule.getRule());
            if(articleId!=null) {
                Article article = articleService.findById(Long.valueOf(articleId));
                return article;
            }
        }
        return null;
    }

    /**
     * 根据站点和Request获得文章对象
     *
     * @param site
     * @param request
     * @return
     * */
    public Article getArticleBySiteAndRequest(Site site,HttpServletRequest request){
        String requestUrl = PatternMatchUtil.getUrl(request);
        Route route=routeResolverService.getRoute(site, requestUrl);
        if(route!=null){
            Integer articleId=PatternMatchUtil.getUrlId(requestUrl, route.getRule());
            if(articleId!=null) {
                Article article = articleService.findById(Long.valueOf(articleId));
                return article;
            }
        }
        return null;
    }
}
