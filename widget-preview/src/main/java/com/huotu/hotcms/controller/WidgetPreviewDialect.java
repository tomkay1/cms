/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.controller;

import com.huotu.hotcms.widget.Widget;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

/**
 * @author CJ
 */
@Component
public class WidgetPreviewDialect implements IDialect, IExpressionObjectDialect, IExpressionObjectFactory {
    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return this;
    }

    @Override
    public String getName() {
        return "widgetPreview";
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.singleton("widgetId");
    }

    public String id(Widget widget) {
        return Widget.URIEncodedWidgetIdentity(widget);
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        return this;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
