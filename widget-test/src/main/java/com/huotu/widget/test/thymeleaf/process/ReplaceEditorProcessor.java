/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf.process;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.WidgetHolder;
import com.huotu.widget.test.thymeleaf.CMSDialect;
import com.huotu.widget.test.thymeleaf.CMSProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.EngineEventUtils;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Set;

/**
 * // <div th:replace="'PREVIEW/'+${widgetId}+'/'+${styleId} :: div "></div>
 * 通过调用{@link org.thymeleaf.TemplateEngine#process(String, Set, IContext)}
 *
 * @author CJ
 */
@Component
public class ReplaceEditorProcessor extends AbstractAttributeTagProcessor implements CMSProcessor {
    @Autowired
    WidgetHolder widgetHolder;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private WidgetService widgetService;

    public ReplaceEditorProcessor() {
        super(TemplateMode.HTML, CMSDialect.Prefix, null, false, "replaceEditor", true, 10000, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,..

        final ThymeleafComponent component =  ExpressionParsingUtilAccess.parseWidgetAndProperties(context, attributeValue);
        String widgetId = component.getWidgetId().execute(context).toString();
        ComponentProperties properties = (ComponentProperties) component.getProperties().execute(context);
        Widget widget = widgetHolder.getWidgetSet().stream().filter(widgetPredicate -> WidgetTestConfig.WidgetIdentity(widgetPredicate)
                .equals(widgetId)).findAny().orElseThrow(() -> new IllegalArgumentException("bad widgetId:" + widgetId));


        // TODO 上下文是CMS生命周期 必不可少的,应该无需在此初始化
        if (!(context instanceof WebEngineContext)) {
            throw new IllegalStateException("TemplateContext should be a WebEngineContext");
        }
        CMSContext cmsContext = new CMSContext();
        cmsContext.setRequest(((WebEngineContext) context).getRequest());
        cmsContext.setResponse(((WebEngineContext) context).getResponse());
        cmsContext.setLocale(context.getLocale());

        structureHandler.replaceWith(widgetService.editorHTML(widget, cmsContext,properties ), false);
    }
}
