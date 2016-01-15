/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import com.huotu.hotcms.web.service.factory.ArticleForeachProcessorFactory;
import com.huotu.hotcms.web.service.factory.CategoryForeachProcessorFactory;
import com.huotu.hotcms.web.service.factory.LinkForeachProcessorFactory;
import com.huotu.hotcms.web.service.factory.VideoForeachProcessorFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;



/**
 * Created by cwb on 2016/1/4.
 */
public class ForeachProcessorService extends BaseProcessorService {


    public Object resolveDataByAttr(IProcessableElementTag elementTag, ITemplateContext context) {
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getDialectPrefix())) {
            return ArticleForeachProcessorFactory.getInstance().process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.LINK.getDialectPrefix())) {
            return LinkForeachProcessorFactory.getInstance().process(elementTag, context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.CATEGORY.getDialectPrefix())) {
            return CategoryForeachProcessorFactory.getInstance().process(elementTag,context);
        }
        if(dialectPrefix.equals(DialectTypeEnum.VIDEO.getDialectPrefix())) {
            return VideoForeachProcessorFactory.getInstance().process(elementTag, context);
        }
        return null;
    }


}
