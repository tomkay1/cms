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
 * Created by Administrator xhl 2016/1/6.
 */
public class SiteHrefProcessorFactory extends BaseProcessorService {


    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        SiteResolveService siteResolveService = (SiteResolveService)applicationContext.getBean("siteResolveService");
        try {
            Site site = siteResolveService.getCurrentSite(request);
//            String attributeName= PatternMatchUtil.getMatchVal(attributeValue, regexp);
//            attributeName= StringUtil.toUpperCase(attributeName);
//            Object object = site.getClass().getDeclaredMethod("get"+attributeName).invoke(site);
            return null;
        }
        catch (Exception ex)
        {
            //写错误日志操作
        }
        return null;
    }
}
