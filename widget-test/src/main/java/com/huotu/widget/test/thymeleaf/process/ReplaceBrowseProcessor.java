/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf.process;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetService;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.WidgetHolder;
import com.huotu.widget.test.thymeleaf.WidgetProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.standard.expression.ExpressionParsingUtilAccess;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.processor.StandardEachTagProcessor;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    @Autowired
    WidgetHolder widgetHolder;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    WidgetService widgetService;

    public ReplaceBrowseProcessor() {
        super(TemplateMode.HTML, "w", null, false, "replaceBrowse", true, 99999, true);
    }


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, IElementTagStructureHandler structureHandler) {
        // 我们的格式应该是 widgetId,styleId,...
        final ThymeleafComponent component = ExpressionParsingUtilAccess.parseThymeleafComponent(context, attributeValue);

        if (!(context instanceof WebEngineContext)){
            throw new IllegalStateException("TemplateContext should be a WebEngineContext");
        }

        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);

        String widgetId = component.getWidgetId().execute(context).toString();
        String styleId = component.getStyleId().execute(context).toString();
        ComponentProperties properties = (ComponentProperties) component.getProperties().execute(context);

        WebEngineContext engineContext = (WebEngineContext) context;
        engineContext.setVariable("widgetId",widgetId);
        engineContext.setVariable("styleId",styleId);
        engineContext.setVariable("properties", properties);
        widgetHolder.getWidgetSet().stream().filter(widget -> WidgetTestConfig.WidgetIdentity(widget)
                .equals(widgetId)).forEach(widget -> engineContext.setVariable("widget", widget));

        String finalHTML = engine.process("TEMPLATE/"+widgetId+"/"+styleId, Collections.singleton("div"), engineContext);
        structureHandler.replaceWith(finalHTML, false);
    }
}
