package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleCurrentProcessorFactory;
import com.huotu.hotcms.web.service.factory.SiteCurrentProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class CurrentProcessorService extends BaseProcessorService {
    public Object resolveDataByAttr(IProcessableElementTag tag,String attributeValue, ITemplateContext context) {
        if (dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticleCurrentProcessorFactory.getInstance().resolveDataByAttr(tag,attributeValue, context);
        }
        if (dialectPrefix.equals(DialectTypeEnum.SITE.getDialectPrefix())) {
            return SiteCurrentProcessorFactory.getInstance().resolveDataByAttr(attributeValue, context);
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

    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        if (dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticleCurrentProcessorFactory.getInstance().resolveDataByAttr(tab,context);
//            return ArticleCurrentProcessorFactory.getInstance().resolveDataByAttr(tag,attributeValue, context);
        }
        if (dialectPrefix.equals(DialectTypeEnum.SITE.getDialectPrefix())) {
//            return SiteCurrentProcessorFactory.getInstance().resolveDataByAttr(attributeValue, context);
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