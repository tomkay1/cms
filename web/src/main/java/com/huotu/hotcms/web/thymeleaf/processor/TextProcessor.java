package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.BaseProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;

/**
 * <P>
 *     自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 *
 */
public class TextProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1300;

    public static final String ATTR_NAME = "text";
    private BaseProcessorService baseProcessorService;

    public TextProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix, BaseProcessorService dialectService) {
        super(dialect,templateMode,dialectPrefix,ATTR_NAME,PRECEDENCE,true);
        baseProcessorService=dialectService;
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

        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");

        
        final String text=baseProcessorService.resolveDataByAttr(request,tag, attributeName, attributeValue);
        structureHandler.setBody(text, false);
    }
}
