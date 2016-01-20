package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.PreviousProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 * 自定义thymeleaf 方言(上一条输出语法方言)
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class PreviousProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 200;

    public static final String ATTR_NAME = "previous";
    private PreviousProcessorService previousProcessorService;

    public PreviousProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.previousProcessorService = new PreviousProcessorService();
        this.previousProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler){
        final Object iteratedValue;
        iteratedValue = previousProcessorService.resolveDataByAttr(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
