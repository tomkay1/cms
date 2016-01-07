package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.SrcProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.expression.*;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public class SrcProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "src";

    private SrcProcessorService srcProcessorService;

    public SrcProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        this.srcProcessorService = new SrcProcessorService();
        this.srcProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             String attributeTemplateName,
                             int attributeLine,
                             int attributeCol,
                             Object expressionResult,
                             IElementTagStructureHandler structureHandler) {

//        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        String newAttributeValue=null;
        Object srcObject=this.srcProcessorService.resolveSrcData(attributeValue,context);
        newAttributeValue=srcObject!=null?srcObject.toString():"";

        newAttributeValue=HtmlEscape.escapeHtml4Xml(newAttributeValue == null ? "" : newAttributeValue.toString());

        // Let RequestDataValueProcessor modify the attribute value if needed
        newAttributeValue = RequestDataValueProcessorUtils.processUrl(context, newAttributeValue);
        // Set the real, non prefixed attribute
        tag.getAttributes().replaceAttribute(attributeName, ATTR_NAME, (newAttributeValue == null? "" : newAttributeValue));
    }
}
