/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service;

import com.huotu.hotcms.web.common.DialectTypeEnum;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cwb on 2016/1/4.
 */
public class TextProcessorService extends BaseProcessorService {


    @Override
    public Object resolveDataByAttr(String attributeValue, ITemplateContext context) {
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
        if(dialectPrefix.equals(DialectTypeEnum.ARTICLE.getValue())) {
            long articleId = Long.parseLong(request.getParameter("articleId"));
            //WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            return "文章article title";
        }
        return null;
    }
}
