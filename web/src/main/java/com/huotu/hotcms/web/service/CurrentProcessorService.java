package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleCurrentProcessorFactory;
import com.huotu.hotcms.web.service.factory.SiteCurrentProcessorFactory;
import com.huotu.hotcms.web.service.factory.VideoCurrentProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * 根据条件当前输出方言解析服务入口
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
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
        }
        if(dialectPrefix.equals(DialectTypeEnum.VIDEO.getDialectPrefix())){
            return VideoCurrentProcessorFactory.getInstance().resolveDataByAttr(tab,context);
        }
        if (dialectPrefix.equals(DialectTypeEnum.SITE.getDialectPrefix())) {
            return null;
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