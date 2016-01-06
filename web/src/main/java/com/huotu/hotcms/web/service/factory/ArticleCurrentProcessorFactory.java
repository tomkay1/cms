package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.RouteRule;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.service.RoutResolverService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class ArticleCurrentProcessorFactory extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        SiteResolveService siteResolveService = (SiteResolveService)applicationContext.getBean("siteResolveService");
        try {
            Site site = siteResolveService.getCurrentSite(request);
            RoutResolverService routResolverService=(RoutResolverService)applicationContext.getBean("routResolverService");
            RouteRule routeRule=routResolverService.getRout(site,PatternMatchUtil.getUrl(request));
            if(routeRule!=null)
            {
                Integer articleId=PatternMatchUtil.getUrlId(PatternMatchUtil.getUrl(request),routeRule.getRule());
                if(articleId!=null) {
                    ArticleServiceImpl articleService = (ArticleServiceImpl) applicationContext.getBean("articleServiceImpl");
                    Article article = articleService.findById(Long.valueOf(articleId));

                    String attributeName = PatternMatchUtil.getMatchVal(attributeValue, regexp);
                    attributeName = StringUtil.toUpperCase(attributeName);
                    Object object = article.getClass().getDeclaredMethod("get" + attributeName).invoke(article);
                    return object;
                }
            }
        }
        catch (Exception ex)
        {
            //写错误日志操作
        }
        return null;
    }
}
