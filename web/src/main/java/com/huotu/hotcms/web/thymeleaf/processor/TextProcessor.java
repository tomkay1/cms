package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.TextProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;


/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class TextProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1300;

    public static final String ATTR_NAME = "text";
    private TextProcessorService textProcessorService;

    public TextProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        this.textProcessorService = new TextProcessorService();
        this.textProcessorService.setDialectPrefix(dialectPrefix);
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

        final String text = (String) textProcessorService.resolveDataByAttr(attributeValue, context);
        structureHandler.setBody(text, false);
    }
}
