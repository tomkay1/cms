/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf.process;

import com.huotu.widget.test.thymeleaf.WidgetProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.standard.processor.StandardEachTagProcessor;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 注意使用{@link IElementTagStructureHandler#replaceWith(CharSequence, boolean)}替换所有
 *
 * @author CJ
 */
@Component
public class ReplaceBrowseProcessor extends AbstractAttributeTagProcessor implements WidgetProcessor {

    private static final Log log = LogFactory.getLog(ReplaceBrowseProcessor.class);
    StandardEachTagProcessor processor;
    StandardReplaceTagProcessor standardReplaceTagProcessor;


    public ReplaceBrowseProcessor() {
        super(TemplateMode.HTML, "w", null, false, "replaceBrowse", true, 10000, true);
    }


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,styleId,...
        final ThymeleafComponent component = ExpressionParsingUtilAccess.parseThymeleafComponent(context, attributeValue);

        StandardEachTagProcessor processor;
//        EachUtils.parseEach()
        // 寻找他们
        String finalHTML = "<span>hello</span>";


        structureHandler.replaceWith(finalHTML, false);

    }
}
