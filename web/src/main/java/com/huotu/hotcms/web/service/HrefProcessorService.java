package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleCurrentProcessorFactory;
import com.huotu.hotcms.web.service.factory.ArticleHrefProcessorFactory;
import com.huotu.hotcms.web.service.factory.SiteCurrentProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.standard.expression.Assignation;

import java.util.List;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class HrefProcessorService extends BaseProcessorService{
    public String resolveLinkData(List<Assignation> assignation,String LinkExpression, ITemplateContext context){
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticleHrefProcessorFactory.getInstance().resolveLinkData(assignation,LinkExpression,context);
        }

        if(dialectPrefix.equals(DialectTypeEnum.CATEGORY.getDialectPrefix())) {
            return null;
        }

        if(dialectPrefix.equals(DialectTypeEnum.LINK.getDialectPrefix()))
        {
            return null;
        }
        return null;
    }
}
