package com.huotu.cms.manage.dialect;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2015/12/18.
 */
public class Java8TimeProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    public static final int PRECEDENCE = 1300;
    public static final String ATTR_NAME = "java8Time";

    protected Java8TimeProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
//        super(dialect, TemplateMode.HTML, dialectPrefix,  ATTR_NAME, PRECEDENCE, true);
        super(TemplateMode.HTML, dialectPrefix,  ATTR_NAME, PRECEDENCE, true);
    }

//    @Override
//    protected void doProcess(ITemplateContext context,
//                             IProcessableElementTag tag,
//                             AttributeName attributeName, String attributeValue,
//                             String attributeTemplateName, int attributeLine, int attributeCol,
//                             Object expressionResult,
//                             IElementTagStructureHandler structureHandler) {
//
//        String text = formatJava8Date(expressionResult);
//        structureHandler.setBody(text, false);
//
//    }


    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             Object expressionResult,
                             IElementTagStructureHandler structureHandler) {
        String text = formatJava8Date(expressionResult);
        structureHandler.setBody(text, false);
    }

    public String formatJava8Date(Object object) {
        if(object instanceof LocalDateTime) {
            return ((LocalDateTime) object).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if(object instanceof LocalDate) {
            return ((LocalDate) object).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if(object instanceof LocalTime) {
            return  ((LocalTime) object).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return "";
    }

}
