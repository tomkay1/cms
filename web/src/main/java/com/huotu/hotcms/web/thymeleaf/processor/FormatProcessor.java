package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.FormatProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 *     格式化方言属性
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 *
 */
public class FormatProcessor  extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 200;


    public static final String ATTR_NAME = "format";
    private FormatProcessorService formatProcessorService;

    public FormatProcessor(final IProcessorDialect dialect,final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix,  ATTR_NAME, PRECEDENCE, true);
        this.formatProcessorService = new FormatProcessorService();
        this.formatProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName, String attributeValue,
                             String attributeTemplateName, int attributeLine, int attributeCol,
                             Object expressionResult,
                             IElementTagStructureHandler structureHandler) {

        Object obj=this.formatProcessorService.resolveDataByAttr(tag,context,expressionResult);
        String text =obj!=null?obj.toString():"";
        structureHandler.setBody(text, false);

    }
}
