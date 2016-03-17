/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.expression.Assignation;

import java.util.List;

/**
 * Created by cwb on 2016/1/4.
 */

public class BaseProcessorService  {

    protected String dialectPrefix;

    public Object  resolveDataByAttr(IProcessableElementTag elementTag,ITemplateContext context){
        return null;
    }
    public Object resolveDataByAttr(String attributeValue,ITemplateContext context){
        return null;
    }

    public String resolveDataByAttr(ITemplateContext context,IProcessableElementTag elementTag,AttributeName attributeName,String attributeValue)
    {
        return  null;
    }

    public String resolveLinkData(List<Assignation> assignations,String LinkExpression, ITemplateContext context){return null;}

    public String getDialectPrefix() {
        return dialectPrefix;
    }

    public void setDialectPrefix(String dialectPrefix) {
        this.dialectPrefix = dialectPrefix;
    }
}
