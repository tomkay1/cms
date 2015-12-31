package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.BaseDialectService;
import com.huotu.hotcms.web.service.ForeachDialectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.Each;
import org.thymeleaf.standard.expression.EachUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;

/**
 * <P>
 *     自定义循环thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 *
 */
public class ForeachProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1200;

    private BaseDialectService baseDialectService;

    public ForeachProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix,final String attrName, BaseDialectService dialectService) {
        super(dialect, templateMode, dialectPrefix, null, false, attrName, true, PRECEDENCE, true);
        baseDialectService=dialectService;
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             String attributeTemplateName,
                             int attributeLine,
                             int attributeCol,
                             IElementTagStructureHandler structureHandler) {

        final Object iteratedValue=baseDialectService.resolveDataByAttr(tag,attributeName);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
