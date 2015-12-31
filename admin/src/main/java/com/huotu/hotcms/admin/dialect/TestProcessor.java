package com.huotu.hotcms.admin.dialect;
import com.huotu.hotcms.admin.model.Seo;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.Each;
import org.thymeleaf.standard.expression.EachUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * 测试
 * Created by Administrator xhl 2015/12/30.
 */
public class TestProcessor extends AbstractAttributeTagProcessor {

    public static final int PRECEDENCE = 1200;
    public static final String ATTR_NAME = "HotEach";

    public TestProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix) {
        super(dialect, templateMode, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
//        super(dialect, TemplateMode.HTML, dialectPrefix,  ATTR_NAME, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final String attributeTemplateName, final int attributeLine, final int attributeCol,
            final IElementTagStructureHandler structureHandler) {
       Object obj= context.getVariable("links");
//        WebContext ctx = new WebContext(context.)
//        Seo[] seo=new Seo[]{
//                new Seo("ceshi 1"),
//                new Seo("ceshi 2"),
//                new Seo("ceshi 3"),
//                new Seo("ceshi 4")
//        };
//       ctx.setVariable("links",seo);

       String s= tag.getAttributes().getValue("data-hot-id");
       Object obj2= context.getVariable("links");

       List<TemplateData> listData= context.getTemplateStack();

//        context.getTemplateMode();
        TemplateData data=context.getTemplateData();
        TemplateMode model= context.getTemplateMode();

        final Each each = EachUtils.parseEach(context, attributeValue);

        final IStandardExpression iterVarExpr = each.getIterVar();
        final IStandardExpression iterAble= each.getIterable();
        final Object iterVarValue = iterVarExpr.execute(context);

//        final IStandardExpression iterVarExpr = each.getIterVar();
//        final Object iterVarValue = iterVarExpr.execute(context);

        final IStandardExpression statusVarExpr = each.getStatusVar();
        final Object statusVarValue;
        if (statusVarExpr != null) {
            statusVarValue = statusVarExpr.execute(context);
        } else {
            statusVarValue = null; // Will provoke the default behaviour: iterVarValue + 'Stat'
        }

        final IStandardExpression iterableExpr = each.getIterable();
        final Object iteratedValue = iterableExpr.execute(context);

        Seo[] seo=new Seo[]{
                new Seo("ceshi 1"),
                new Seo("ceshi 2"),
                new Seo("ceshi 3"),
                new Seo("ceshi 4"),
                new Seo("ceshi 5")
        };


//        iterVarExpr.execute()

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
//        structureHandler.iterateElement(iterVarName, statusVarName, iteratedValue);

        structureHandler.iterateElement(iterVarName, statusVarName,seo);
    }

}
