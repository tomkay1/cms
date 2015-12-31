package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.service.BaseDialectService;
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
 *     自定义循环thymeleaf 语法标签解析基类
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 *
 */
public class BaseProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1200;
    public static final String ATTR_NAME = "foreach";

    private BaseDialectService baseDialectService;


    public BaseProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix, BaseDialectService dialectService) {
        super(dialect, templateMode, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
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

        final Each each = EachUtils.parseEach(context, attributeValue);

        final IStandardExpression iterVarExpr = each.getIterVar();
        final Object iterVarValue = iterVarExpr.execute(context);

        final IStandardExpression statusVarExpr = each.getStatusVar();
        final Object statusVarValue;
        if (statusVarExpr != null) {
            statusVarValue = statusVarExpr.execute(context);
        } else {
            statusVarValue = null; // Will provoke the default behaviour: iterVarValue + 'Stat'
        }

        final IStandardExpression iterableExpr = each.getIterable();

        //根据Tag来解析数据
        final Object iteratedValue=baseDialectService.resolveDataByAttr(tag);

        final String iterVarName = (iterVarValue == null? null : iterVarValue.toString());
        if (StringUtils.isEmptyOrWhitespace(iterVarName)) {
            throw new TemplateProcessingException(
                    "Iteration variable name expression evaluated as null: \"" + iterVarExpr + "\"");
        }

        final String statusVarName = (statusVarValue == null? null : statusVarValue.toString());
        if (statusVarExpr != null && StringUtils.isEmptyOrWhitespace(statusVarName)) {
            throw new TemplateProcessingException(
                    "Status variable name expression evaluated as null or empty: \"" + statusVarExpr + "\"");
        }

        structureHandler.iterateElement(iterVarName, statusVarName, iteratedValue);
    }
}
