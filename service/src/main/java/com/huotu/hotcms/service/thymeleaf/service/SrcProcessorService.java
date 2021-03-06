/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.thymeleaf.common.DialectTypeEnum;
import com.huotu.hotcms.service.thymeleaf.service.factory.ArticleSrcProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;

/**
 * Created by Administrator xhl 2016/1/7.
 */
@Component
public class SrcProcessorService {

    @Autowired
    private ArticleSrcProcessor articleSrcProcessor;

    public Object resolveSrcData(String dialectPrefix, String attributeValue, ITemplateContext context) {
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return articleSrcProcessor.resolveDataByAttr(attributeValue, context);
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
