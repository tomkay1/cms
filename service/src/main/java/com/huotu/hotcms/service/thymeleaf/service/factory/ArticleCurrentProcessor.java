/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.current.ArticleCurrentParam;
import com.huotu.hotcms.service.service.ArticleService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import com.huotu.hotcms.service.util.StringUtil;
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
 * 自定义thymeleaf 语法标签解析
 * 当前文章输出方言
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class ArticleCurrentProcessor {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(ArticleCurrentProcessor.class);

    @Autowired
    ArticleService articleService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

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
        Article article=null;
        try{
            article=(Article) VariableExpression.getVariable(context, "article");
            Site site = (Site)VariableExpression.getVariable(context,"site");
            if(article!=null){
                return article;
            }else{
                ArticleCurrentParam articleCurrentParam =dialectAttributeFactory.getForeachParam(tab
                        , ArticleCurrentParam.class);
                HttpServletRequest request = ((IWebContext)context).getRequest();
                String servletUrl=PatternMatchUtil.getServletUrl(request);
                if(articleCurrentParam!=null){//根据当前请求的Uri来获得指定的ID
                    if(articleCurrentParam.getId()==null){
                        articleCurrentParam.setId(PatternMatchUtil.getUrlIdByLongType(servletUrl
                                , PatternMatchUtil.urlParamRegexp));
                    }
                }
                article=articleService.getArticleByParam(articleCurrentParam,site);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
