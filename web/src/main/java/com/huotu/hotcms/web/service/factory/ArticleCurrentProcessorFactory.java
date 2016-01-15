package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.model.thymeleaf.ArticleForeachParam;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.service.util.StringUtil;
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
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class ArticleCurrentProcessorFactory extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(ArticleCurrentProcessorFactory.class);

    private static ArticleCurrentProcessorFactory instance;

    private ArticleCurrentProcessorFactory() {
    }

    public static ArticleCurrentProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleCurrentProcessorFactory();
        }
        return instance;
    }

    public Object resolveDataByAttr(IProcessableElementTag tag,String attributeValue, ITemplateContext context){
        Article article=(Article) VariableExpression.getVariable(context, "article");
        try {
            if(article!=null) {
                String attributeName = PatternMatchUtil.getMatchVal(attributeValue, regexp);
                attributeName = StringUtil.toUpperCase(attributeName);
                Object object = article.getClass().getMethod("get" + attributeName).invoke(article);
                return object;
            }else{//根据当前环境获得文章对象信息

            }
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return "";
    }

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        try{
            Article article=(Article) VariableExpression.getVariable(context, "article");
            if(article!=null){
                return article;
            }else{
                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
                ArticleService articleService = (ArticleService)applicationContext.getBean("articleServiceImpl");
                ArticleCurrentParam articleCurrentParam = DialectAttributeFactory.getInstance().getForeachParam(tab, ArticleCurrentParam.class);
                if(articleCurrentParam!=null){
                    return articleService.findById(articleCurrentParam.getId());
                }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
