/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.thymeleaf.expression;

import com.huotu.hotcms.web.common.ParamEnum;
import com.huotu.hotcms.web.thymeleaf.model.ArticleForeachParam;
import org.springframework.util.StringUtils;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IElementAttributes;
import org.thymeleaf.model.IProcessableElementTag;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by cwb on 2016/1/5.
 */
public class DialectAttributeFactory {

    private static DialectAttributeFactory instance;

    private DialectAttributeFactory() {
    }

    public static DialectAttributeFactory getInstance() {
        if(instance == null) {
            instance = new DialectAttributeFactory();
        }
        return instance;
    }

    public <T>T getForeachParam(IProcessableElementTag elementTag, Class<T> t) throws Exception{
        Object obj = t.newInstance();
        IElementAttributes elementAttributes = elementTag.getAttributes();
        List<AttributeName> attributeNames = elementAttributes.getAllAttributeNames();
        for(AttributeName attr : attributeNames) {
            String paramValue = elementAttributes.getValue(attr);
            Field field = t.getDeclaredField(attr.getAttributeName());
            field.setAccessible(true);
            field.set(obj,paramValue);
        }
        return (T)obj;
    }


}
