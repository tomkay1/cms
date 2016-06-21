/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf.process;

import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.WidgetHolder;
import com.huotu.widget.test.thymeleaf.WidgetProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.Set;

/**
 * // <div th:replace="'TEMPLATE/'+${widgetId}+'/'+${styleId} :: div "></div>
 * 通过调用{@link org.thymeleaf.TemplateEngine#process(String, Set, IContext)}
 *
 * @author CJ
 */
@Component
public class ReplaceEditorProcessor extends AbstractAttributeTagProcessor implements WidgetProcessor {
    @Autowired
    WidgetHolder widgetHolder;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    WidgetService widgetService;

    public ReplaceEditorProcessor() {
        super(TemplateMode.HTML, "w", null, false, "replaceEditor", true, 10000, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,styleId,...
        final ThymeleafComponent component = ExpressionParsingUtilAccess.parseThymeleafComponent(context, attributeValue);
        if (!(context instanceof WebEngineContext)){
            throw new IllegalStateException("TemplateContext should be a WebEngineContext");
        }
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        String widgetId = component.getWidgetId().execute(context).toString();
        WebEngineContext engineContext = (WebEngineContext) context;
        engineContext.setVariable("widgetId",widgetId);
        widgetHolder.getWidgetSet().stream().filter(widget -> WidgetTestConfig.WidgetIdentity(widget)
                .equals(widgetId)).forEach(widget -> engineContext.setVariable("widget", widget));

        String finalHTML = engine.process("EDITOR/"+widgetId , Collections.singleton("div"), engineContext);
        structureHandler.replaceWith(finalHTML, false);
    }
}
