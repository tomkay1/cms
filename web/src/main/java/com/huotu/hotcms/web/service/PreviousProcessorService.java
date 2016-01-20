package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleNextProcessorFactory;
import com.huotu.hotcms.web.service.factory.ArticlePreviousProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * 上一条输出方言解析服务入口
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class PreviousProcessorService extends BaseProcessorService {
    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        if (dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticlePreviousProcessorFactory.getInstance().resolveDataByAttr(tab,context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.VIDEO.getDialectPrefix())){
//            return VideoCurrentProcessorFactory.getInstance().resolveDataByAttr(tab,context);
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
