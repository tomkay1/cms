package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.BaseDialectService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.context.WebExpressionContext;
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
@SuppressWarnings("ConstantConditions")
public class TextProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1300;
//    public static final String ATTR_NAME = "text";

    private BaseDialectService baseDialectService;

//    public WebEngineContext

    public TextProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix, final String attrName, BaseDialectService dialectService) {
        super(dialect,templateMode,dialectPrefix,attrName,PRECEDENCE,true);
        baseDialectService=dialectService;
    }

//    public StandardTextTagProcessor(final IProcessorDialect dialect, final TemplateMode templateMode, final String dialectPrefix) {
//        super(dialect, templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
//    }

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


//        WebEngineContext webEngineContext=(WebEngineContext)context;
        IExpressionObjects expressContent= context.getExpressionObjects();
        HttpServletRequest request=(HttpServletRequest)expressContent.getObject("request");

        final String text=baseDialectService.resolveDataByAttr(request,tag, attributeName, attributeValue);
        structureHandler.setBody(text, false);
    }
}
