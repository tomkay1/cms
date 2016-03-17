/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.thymeleaf.common.DialectTypeEnum;
import com.huotu.hotcms.service.thymeleaf.service.factory.ArticleNextProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * 下一条输出方言解析服务入口
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class NextProcessorService extends BaseProcessorService {
    public Object resolveDataByAttr(IProcessableElementTag tab,ITemplateContext context){
        if (dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticleNextProcessorFactory.getInstance().resolveDataByAttr(tab,context);
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
