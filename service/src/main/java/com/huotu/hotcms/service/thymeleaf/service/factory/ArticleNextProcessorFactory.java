/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticleNextParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.util.PatternMatchUtil;
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
