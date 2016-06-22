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
import com.huotu.hotcms.widget.WidgetResolveService;
import com.huotu.hotcms.widget.loader.thymeleaf.CMSDialect;
import com.huotu.hotcms.widget.loader.thymeleaf.CMSProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 注意使用{@link IElementTagStructureHandler#replaceWith(CharSequence, boolean)}替换所有
 * <p>
 * 作为浏览用处,它仅支持的参数为{@link com.huotu.hotcms.widget.Component}
 *
 * @author CJ
 */
@Component
public class ReplaceBrowseProcessor extends AbstractStandardExpressionAttributeTagProcessor implements CMSProcessor {

    private static final Log log = LogFactory.getLog(ReplaceBrowseProcessor.class);
    @Autowired
    private WidgetResolveService widgetResolveService;

    public ReplaceBrowseProcessor() {
        super(TemplateMode.HTML, CMSDialect.Prefix, "replaceBrowse", 10000, false);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName
            , String attributeValue, Object expressionResult, IElementTagStructureHandler structureHandler) {
        if (expressionResult == null)
            return;
        if (!(expressionResult instanceof com.huotu.hotcms.widget.Component)) {
            log.error("replaceBrowse only support Component value");
            throw new IllegalStateException("replaceBrowse only support Component value");
        }

        String htmlCode = widgetResolveService.componentHTML((com.huotu.hotcms.widget.Component) expressionResult
                , CMSContext.RequestContext());

        structureHandler.replaceWith(htmlCode, false);
    }

}
