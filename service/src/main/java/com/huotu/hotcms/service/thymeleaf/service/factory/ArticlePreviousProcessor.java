/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.model.thymeleaf.next.ArticlePreviousParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     文章上一篇解析工厂
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class ArticlePreviousProcessor {
    private static final Log log = LogFactory.getLog(ArticlePreviousProcessor.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        try{
//            Article article=(Article) VariableExpression.getVariable(context, "article");
            Article article=(Article) context.getVariable("article");
            ArticlePreviousParam articlePreviousParam;
            if(article!=null){
                articlePreviousParam=new ArticlePreviousParam();
                articlePreviousParam.setId(article.getId());
            }else{
                articlePreviousParam = dialectAttributeFactory.getForeachParam(tab
                        , ArticlePreviousParam.class);
                HttpServletRequest request = ((IWebContext)context).getRequest();
                String servletUrl= PatternMatchUtil.getServletUrl(request);
                if(articlePreviousParam!=null){//根据当前请求的Uri来获得指定的ID
                    if(articlePreviousParam.getId()==null){
                        articlePreviousParam.setId(PatternMatchUtil.getUrlIdByLongType(servletUrl
                                , PatternMatchUtil.urlParamRegexp));
                    }
                }
            }
            return  articleService.getArticlePreiousByParam(articlePreviousParam);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
