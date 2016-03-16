/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleHrefProcessorFactory;
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
