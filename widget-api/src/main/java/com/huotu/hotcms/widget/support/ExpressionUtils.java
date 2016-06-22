/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.support;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * 应用在Thymeleaf中表达式的一些工具
 *
 * @author CJ
 */
public class ExpressionUtils {

    /**
     * 处理传入的表达式,并且计算它的结果,如果失败则返回表达式本身
     *
     * @param context 处理上下文
     * @param input   表达式
     * @return 表达式的结果或者表达式本身
     */
    public static Object ParseInputElseInput(ITemplateContext context, String input) {
        IStandardExpressionParser parser = StandardExpressions.getExpressionParser(context.getConfiguration());
        try {
            IStandardExpression expression = parser.parseExpression(context, input);
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
