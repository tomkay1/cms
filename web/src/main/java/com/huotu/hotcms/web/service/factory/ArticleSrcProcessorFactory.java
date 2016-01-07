package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.service.RouteResolverService;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public class ArticleSrcProcessorFactory extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        try {
            Site site = (Site) VariableExpression.getVariable(context, "site");
            RouteResolverService routeResolverService=(RouteResolverService)applicationContext.getBean("routResolverService");
            Route routeRule=routeResolverService.getRoute(site, PatternMatchUtil.getUrl(request));
            if(routeRule!=null){
                Integer articleId=PatternMatchUtil.getUrlId(PatternMatchUtil.getUrl(request),routeRule.getRule());
                if(articleId!=null) {
                    ArticleServiceImpl articleService = (ArticleServiceImpl) applicationContext.getBean("articleServiceImpl");
                    Article article = articleService.findById(Long.valueOf(articleId));

                    String attributeName = PatternMatchUtil.getMatchVal(attributeValue, regexp);
                    attributeName = StringUtil.toUpperCase(attributeName);
                    Object object = article.getClass().getMethod("get" + attributeName).invoke(article);
                    return object;
                }
            }
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return "";
    }
}
