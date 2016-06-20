/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package org.thymeleaf.standard.expression;

import com.huotu.widget.test.thymeleaf.process.ThymeleafComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.context.ITemplateContext;

/**
 * @author CJ
 */
public class ExpressionParsingUtilAccess {

    private static final Log log = LogFactory.getLog(ExpressionParsingUtilAccess.class);

    public static ThymeleafComponent parseThymeleafComponent(ITemplateContext context, String input) {
//        final String preprocessedInput =
//                StandardExpressionPreprocessor.preprocess(context, input);

        ExpressionParsingState state = ExpressionParsingUtil.decompose(input);

        if (state.size() != 4) {
            log.warn("bad replaceBrowse value:" + input);
            throw new IllegalArgumentException(input);
        }

        ThymeleafComponent component = new ThymeleafComponent();
        component.setWidgetId(state.get(1).getExpression());
        component.setStyleId(state.get(2).getExpression());
        component.setProperties(state.get(3).getExpression());
//        if (state.hasExpressionAt(3))


//        String[] names = input.split(",");


        return component;
    }

}
