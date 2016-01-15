package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.CurrentProcessorService;
import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
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
public class CurrentProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 800;

    public static final String ATTR_NAME = "current";
    private CurrentProcessorService currentProcessorService;

    public CurrentProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.currentProcessorService = new CurrentProcessorService();
        this.currentProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler){
        final Object iteratedValue;
        iteratedValue = currentProcessorService.resolveDataByAttr(tag, context);
        if(iteratedValue instanceof Page) {

        }
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
