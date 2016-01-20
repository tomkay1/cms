package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticleNextParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     文章下一篇解析工厂
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class ArticleNextProcessorFactory {
    private static final Log log = LogFactory.getLog(ArticleNextProcessorFactory.class);

    private static ArticleNextProcessorFactory instance;

    private ArticleNextProcessorFactory() {
    }

    public static ArticleNextProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleNextProcessorFactory();
        }
        return instance;
    }

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        try{
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            ArticleService articleService = (ArticleService)applicationContext.getBean("articleServiceImpl");
            Article article=(Article) VariableExpression.getVariable(context, "article");
            ArticleNextParam articleNextParam;
            if(article!=null){
                articleNextParam=new ArticleNextParam();
                articleNextParam.setId(article.getId());
            }else{
                articleNextParam = DialectAttributeFactory.getInstance().getForeachParam(tab, ArticleNextParam.class);
                HttpServletRequest request = ((IWebContext)context).getRequest();
                String servletUrl= PatternMatchUtil.getServletUrl(request);
                if(articleNextParam!=null){//根据当前请求的Uri来获得指定的ID
                    if(articleNextParam.getId()==null){
                        articleNextParam.setId(PatternMatchUtil.getUrlIdByLongType(servletUrl,PatternMatchUtil.urlParamRegexp));
                    }
                }
            }
            return  articleService.getArticleNextByParam(articleNextParam);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
