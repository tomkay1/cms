package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.standard.expression.Assignation;

import java.util.List;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class ArticleHrefProcessorFactory extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    @Override
    public String resolveLinkData(List<Assignation> assignations, String LinkExpression, ITemplateContext context) {
        try {
            if(!StringUtils.isEmpty(LinkExpression)) {
//                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
//                ArticleResolveService articleResolveService=(ArticleResolveService)applicationContext.getBean("articleResolveService");
//                Article article=articleResolveService.getArticleByContent(context);
                Article article=(Article) VariableExpression.getVariable(context, "article");
                if(article!=null){
                    for (Assignation assignation : assignations) {
                        String left = "{"+assignation.getLeft().toString()+"}";
                        String right = assignation.getRight().toString();

                        String attributeName = PatternMatchUtil.getMatchVal(right, regexp);
                        attributeName = StringUtil.toUpperCase(attributeName);
                        Object object = article.getClass().getMethod("get" + attributeName).invoke(article);
                        LinkExpression=LinkExpression.replace(left,object.toString());
                    }
                }
            }
        }
        catch (Exception ex){
           log.equals(ex.getMessage());
        }
        return LinkExpression;
    }
}
