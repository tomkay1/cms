package com.huotu.hotcms.web.service;


import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
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
