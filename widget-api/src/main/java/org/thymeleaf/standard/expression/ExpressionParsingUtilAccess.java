/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package org.thymeleaf.standard.expression;

import com.huotu.hotcms.widget.loader.thymeleaf.process.ThymeleafComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.thymeleaf.context.ITemplateContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class ExpressionParsingUtilAccess {

    private static final Log log = LogFactory.getLog(ExpressionParsingUtilAccess.class);

    /**
     * 将输入字符串解析成数个表达式
     *
     * @param context 上下文
     * @param input   字符串
     * @return 表达式List
     */
    @NotNull
    public static List<com.huotu.hotcms.widget.support.ExpressionParsingNode>
    parsingNodes(ITemplateContext context, String input) {
        ExpressionParsingState state = ExpressionParsingUtil.decompose(input);
        ArrayList<com.huotu.hotcms.widget.support.ExpressionParsingNode> nodes = new ArrayList<>();
        // state 0 保存着是整个表达式的结构,这里我们先忽略。
        for (int i = 1; i < state.size(); i++) {
            nodes.add(new com.huotu.hotcms.widget.support.ExpressionParsingNode(state.get(i).getInput()
                    , state.get(i).getExpression()));
        }
        return nodes;
    }


    public static ThymeleafComponent parseThymeleafComponent(ITemplateContext context, String input) {
        ExpressionParsingNode node;
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

        return component;
    }

    public static ThymeleafComponent parseWidgetAndProperties(ITemplateContext context, String input) {
//        final String preprocessedInput =
//                StandardExpressionPreprocessor.preprocess(context, input);

        ExpressionParsingState state = ExpressionParsingUtil.decompose(input);

        if (state.size() != 3) {
            log.warn("bad replaceBrowse value:" + input);
            throw new IllegalArgumentException(input);
        }

        ThymeleafComponent component = new ThymeleafComponent();
        component.setWidgetId(state.get(1).getExpression());
        component.setProperties(state.get(2).getExpression());
        return component;
    }


}
