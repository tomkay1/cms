/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.support;

import lombok.Data;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;

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
    public Object executeElseInput(ITemplateContext context) {
        try {
            if (expression == null)
                return null;
            if (expression instanceof FragmentExpression) {
                // This is merely a FragmentExpression (not complex, not combined with anything), so we can apply a shortcut
                // so that we don't require a "null" result for this expression if the template does not exist. That will
                // save a call to resource.exists() which might be costly.

                final FragmentExpression.ExecutedFragmentExpression executedFragmentExpression =
                        FragmentExpression.createExecutedFragmentExpression(context, (FragmentExpression) expression
                                , StandardExpressionExecutionContext.NORMAL);

                return FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true);
            }
            return expression.execute(context);
        } catch (Exception ignored) {
            return input;
        }
    }

}
