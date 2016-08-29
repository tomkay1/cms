/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.resolve;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.PreProcessWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring4.naming.SpringContextVariableNames;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author CJ
 */
public class WidgetContext extends WebEngineContext {
    /**
     * <p>
     * 控件生成HTML代码时所使用的上下文,这个上下文继承自{@link IContext}表明可以直接适用于Template环境
     * </p>
     * <p>
     * Creates a new instance of this {@link IEngineContext} implementation binding engine execution to
     * the Servlet API.
     * </p>
     * <p>
     * Note that implementations of {@link IEngineContext} are not meant to be used in order to execute
     * the template engine (use implementations of {@link IContext} such as {@link Context} or {@link WebContext}
     * instead). This is therefore mostly an <b>internal</b> implementation, and users should have no reason
     * to ever call this constructor except in very specific integration/extension scenarios.
     * </p>
     *
     * @param engine         我们所用的专用引擎
     * @param context        CMS上下文不可为空
     * @param widget         控件实例不可为空
     * @param style          控件风格可选
     * @param servletContext the servlet context object.
     * @param component      组件
     * @param properties
     */
    public WidgetContext(SpringTemplateEngine engine, CMSContext context, Widget widget, WidgetStyle style
            , ServletContext servletContext, Component component, ComponentProperties properties) {
        // 某些属性需要传染下去 比如来自CMSContext
        super(new WidgetConfiguration(engine.getConfiguration(), widget, style)
                , null, null
                , context.getRequest(), context.getResponse(), servletContext
                , context.getLocale(), FromComponentProperties(context, widget, style, properties, component));
    }

    private static Map<String, Object> FromComponentProperties(CMSContext context, Widget widget, WidgetStyle style
            , ComponentProperties properties, Component component) {
        Map<String, Object> variables = new HashMap<>();
        if (context.getCurrentPage() != null) {
            variables.put("uri", context.getCurrentPage().getPagePath());
        }
        variables.put("widget", widget);
        variables.put("style", style);
        variables.put("properties", properties);
        variables.put("site", CMSContext.RequestContext().getSite());
        if (properties != null) {
            Iterator<Map.Entry<String, Object>> entries = properties.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                variables.put(entry.getKey(), entry.getValue());
            }
        }
        if (component != null) {
            variables.put("componentId", component.getId());
        } else {
            variables.put("componentId", "");
        }
        // "thymeleaf::EvaluationContext" ->
        // "springMacroRequestContext" ->
        // "springRequestContext" ->
//        variables.
        context.widgetContextVariables(variables);
        RequestContext context1 = context.getRequestContext();
        variables.put(SpringContextVariableNames.SPRING_REQUEST_CONTEXT, context1);
        variables.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, context1);

        final ConversionService conversionService =
                (ConversionService) context.getRequest().getAttribute(ConversionService.class.getName()); // might be null!

        final ThymeleafEvaluationContext evaluationContext =
                new ThymeleafEvaluationContext(WebApplicationContextUtils.findWebApplicationContext(
                        context.getRequest().getServletContext())
                        , conversionService);
        variables.put(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);

//        ThymeleafView # renderFragment
        if (widget instanceof PreProcessWidget) {
            ((PreProcessWidget) widget).prepareContext(style, properties, variables
                    , context.getWidgetParameters(component));
        }
        return variables;
    }


}
