package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.HrefProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.expression.*;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class HrefProcessor  extends AbstractStandardExpressionAttributeTagProcessor
{
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "href";

    private HrefProcessorService hrefProcessorService;

    public HrefProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        this.hrefProcessorService = new HrefProcessorService();
        this.hrefProcessorService.setDialectPrefix(dialectPrefix);
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
//        IExpressionObjects expressContent= context.getExpressionObjects();
//        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");
//        String newAttributeValue = HtmlEscape.escapeHtml4Xml(expressionResult == null ? "" : expressionResult.toString());
        String newAttributeValue=null;

        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());

        if (attributeValue != null) {
            final IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
            AssignationSequence assignations=((LinkExpression) expression).getParameters();
            List<Assignation> list= assignations.getAssignations();
            String linkExpression=((LinkExpression) expression).getBase().toString();//获得链接Template
            newAttributeValue=this.hrefProcessorService.resolveLinkData(list,linkExpression,context);
        } else {
            expressionResult = null;
        }

        // Let RequestDataValueProcessor modify the attribute value if needed
        newAttributeValue = RequestDataValueProcessorUtils.processUrl(context, newAttributeValue);

        // Set the real, non prefixed attribute
        tag.getAttributes().replaceAttribute(attributeName, ATTR_NAME, (newAttributeValue == null? "" : newAttributeValue));

    }
}
