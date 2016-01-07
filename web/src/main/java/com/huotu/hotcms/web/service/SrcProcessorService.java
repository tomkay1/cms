package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleHrefProcessorFactory;
import com.huotu.hotcms.web.service.factory.ArticleSrcProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.standard.expression.Assignation;

import java.util.List;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public class SrcProcessorService extends BaseProcessorService{

    public Object resolveSrcData(String attributeValue, ITemplateContext context){
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return new ArticleSrcProcessorFactory().resolveDataByAttr(attributeValue,context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.SITE.getDialectPrefix())) {
            return null;
        }
        if(dialectPrefix.equals(DialectTypeEnum.CATEGORY.getDialectPrefix())) {
            return null;
        }
        if(dialectPrefix.equals(DialectTypeEnum.DOWNLOAD.getDialectPrefix()))
        {
            return null;
        }
        if(dialectPrefix.equals(DialectTypeEnum.GALLERY.getDialectPrefix()))
        {
            return null;
        }
        if(dialectPrefix.equals(DialectTypeEnum.LINK.getDialectPrefix()))
        {
            return null;
        }
        if(dialectPrefix.equals(DialectTypeEnum.NOTICE.getDialectPrefix()))
        {
            return null;
        }
        return null;
    }
}
