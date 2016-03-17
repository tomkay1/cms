/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.thymeleaf.service.BaseProcessorService;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import com.huotu.hotcms.service.util.StringUtil;
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

    private static ArticleHrefProcessorFactory instance;

    private ArticleHrefProcessorFactory() {
    }

    public static ArticleHrefProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ArticleHrefProcessorFactory();
        }
        return instance;
    }

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
