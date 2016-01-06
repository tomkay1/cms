package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleCurrentProcessorFactory;
import com.huotu.hotcms.web.service.factory.SiteCurrentProcessorFactory;
import org.thymeleaf.context.ITemplateContext;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class CurrentProcessorService extends BaseProcessorService {
    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return new ArticleCurrentProcessorFactory().resolveDataByAttr(attributeValue,context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.SITE.getDialectPrefix())) {
            return new SiteCurrentProcessorFactory().resolveDataByAttr(attributeValue,context);
        }
        return null;
    }
}
