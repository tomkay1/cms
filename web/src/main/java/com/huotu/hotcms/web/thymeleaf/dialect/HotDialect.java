package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.thymeleaf.expression.HotExpressionObjectFactory;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExecutionAttributeDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.spring4.expression.SPELVariableExpressionEvaluator;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardVariableExpressionEvaluator;

import java.util.Map;
import java.util.Set;

/**
 * Created by cwb on 2015/12/31.
 */
public class HotDialect extends StandardDialect {

    public static final String NAME = "hot";
    public static final String PREFIX = "th";
    public static final int PROCESSOR_PRECEDENCE = 1000;


    private final IExpressionObjectFactory HOT_EXPRESSION_OBJECTS_FACTORY = new HotExpressionObjectFactory();

    public HotDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Map<String, Object> getExecutionAttributes() {
        return null;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return HOT_EXPRESSION_OBJECTS_FACTORY;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return StandardDialect.createStandardProcessorsSet(this, dialectPrefix);
    }
}
