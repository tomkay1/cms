/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.loader.thymeleaf;

import lombok.Data;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.standard.expression.Expression;

/**
 * @author CJ
 */
@Data
public class ExpressionParsingNode {

    private final String input;
    private final Expression expression;

    /**
     * 解析表达式,如果失败就返回input
     *
     * @param context 处理表达式的上下文
     * @return 表达式的结果或者可能是Input
     */
    public Object executeElseInput(IExpressionContext context) {
        try {
            return expression.execute(context);
        } catch (Exception ignored) {
            return input;
        }
    }

}
