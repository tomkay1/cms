/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.loader.thymeleaf.process;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.loader.thymeleaf.CMSDialect;
import com.huotu.hotcms.widget.loader.thymeleaf.CMSProcessor;
import com.huotu.hotcms.widget.loader.thymeleaf.ExpressionParsingNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;
import java.util.Set;

/**
 * 通过调用{@link org.thymeleaf.TemplateEngine#process(String, Set, IContext)}
 * replaceEditor比较灵活,支持多种参数
 * Widget
 * Widget,Properties
 * String(widget唯一码)
 * String(widget唯一码),Properties
 * String,String,String,Properties
 * String,String,String
 *
 * @author CJ
 */
@Component
public class ReplaceEditorProcessor extends AbstractAttributeTagProcessor implements CMSProcessor {

    @Autowired
    private WidgetLocateService widgetLocateService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WidgetResolveService widgetResolveService;

    public ReplaceEditorProcessor() {
        super(TemplateMode.HTML, CMSDialect.Prefix, null, false, "replaceEditor", true, 10000, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,..
        List<ExpressionParsingNode> nodeList = ExpressionParsingUtilAccess.parsingNodes(context, attributeValue);
        CMSContext cmsContext = CMSContext.RequestContext();

        Widget widget;
        ComponentProperties properties;
        if (nodeList.size() > 2) {
            //groupId,widgetId,version
            String groupId = (String) nodeList.get(0).executeElseInput(context);
            String widgetId = (String) nodeList.get(1).executeElseInput(context);
            String version = (String) nodeList.get(2).executeElseInput(context);

            widget = widgetLocateService.findWidget(groupId, widgetId, version).getWidget();
            properties = null;
            if (nodeList.size() > 3)
                properties = (ComponentProperties) nodeList.get(3).getExpression().execute(context);
        } else {
            // 第一个参数
            Object widgetObject = nodeList.get(0).executeElseInput(context);

            if (widgetObject instanceof Widget) {
                widget = (Widget) widgetObject;
            } else {
                widget = widgetLocateService.findWidget(widgetObject.toString()).getWidget();
            }
            properties = null;
            if (nodeList.size() > 1)
                properties = (ComponentProperties) nodeList.get(1).getExpression().execute(context);
        }

        structureHandler.replaceWith(widgetResolveService.editorHTML(widget, cmsContext, properties), false);
    }
}
