package com.huotu.hotcms.admin.dialect;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/17.
 */
public class HotDialect extends AbstractProcessorDialect {

    public static final String NAME = "huotu";
    public static final String PREFIX = "hot";
    public static final int PROCESSOR_PRECEDENCE = 900;

    public HotDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createHotProcessorsSet(this,dialectPrefix);
    }

    public static Set<IProcessor> createHotProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {

        Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new Java8TimeProcessor(dialect, dialectPrefix));

        return processors;
    }
}
