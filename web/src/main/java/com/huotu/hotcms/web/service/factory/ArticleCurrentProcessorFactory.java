package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.ITemplateContext;

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

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    private static ArticleCurrentProcessorFactory instance;

    private ArticleCurrentProcessorFactory() {
    }

    public static ArticleCurrentProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleCurrentProcessorFactory();
        }
        return instance;
    }

    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
//        WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
//        ArticleResolveService articleResolveService=(ArticleResolveService)applicationContext.getBean("articleResolveService");
//        Article article=articleResolveService.getArticleByContent(context);
        Article article=(Article) VariableExpression.getVariable(context, "article");
        try {
            if(article!=null) {
                String attributeName = PatternMatchUtil.getMatchVal(attributeValue, regexp);
                attributeName = StringUtil.toUpperCase(attributeName);
                Object object = article.getClass().getMethod("get" + attributeName).invoke(article);
                return object;
            }
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return "";
    }
}
