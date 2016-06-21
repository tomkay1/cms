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
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.WidgetHolder;
import com.huotu.widget.test.thymeleaf.CMSDialect;
import com.huotu.widget.test.thymeleaf.CMSProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 注意使用{@link IElementTagStructureHandler#replaceWith(CharSequence, boolean)}替换所有
 *
 * @author CJ
 */
@Component
public class ReplaceBrowseProcessor extends AbstractAttributeTagProcessor implements CMSProcessor {

    private static final Log log = LogFactory.getLog(ReplaceBrowseProcessor.class);
    @Autowired
    private WidgetHolder widgetHolder;
    @Autowired
    private WidgetService widgetService;

    public ReplaceBrowseProcessor() {
        super(TemplateMode.HTML, CMSDialect.Prefix, null, false, "replaceBrowse", true, 99999, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,styleId,...
        final ThymeleafComponent component = ExpressionParsingUtilAccess.parseThymeleafComponent(context, attributeValue);

        String widgetId = component.getWidgetId().execute(context).toString();
        String styleId = component.getStyleId().execute(context).toString();
        ComponentProperties properties = (ComponentProperties) component.getProperties().execute(context);
        Widget widget = widgetHolder.getWidgetSet().stream().filter(widgetPredicate -> WidgetTestConfig.WidgetIdentity(widgetPredicate)
                .equals(widgetId)).findAny().orElseThrow(() -> new IllegalArgumentException("bad widgetId:" + widgetId));

        InstalledWidget installedWidget = new InstalledWidget();
        installedWidget.setType("unknown");
        installedWidget.setWidget(widget);

        // TODO 组件应该是参数给予的,而非自己构造
        com.huotu.hotcms.widget.Component component1 = new com.huotu.hotcms.widget.Component();
        component1.setWidget(installedWidget);
        component1.setStyleId(styleId);
        component1.setProperties(properties);

        if (!(context instanceof WebEngineContext)) {
            throw new IllegalStateException("TemplateContext should be a WebEngineContext");
        }
        // TODO 上下文是CMS生命周期 必不可少的,应该无需在此初始化
        CMSContext cmsContext = new CMSContext();
        cmsContext.setRequest(((WebEngineContext) context).getRequest());
        cmsContext.setResponse(((WebEngineContext) context).getResponse());
        cmsContext.setLocale(context.getLocale());

        String htmlCode = widgetService.componentHTML(component1, cmsContext);

        structureHandler.replaceWith(htmlCode, false);
    }
}
