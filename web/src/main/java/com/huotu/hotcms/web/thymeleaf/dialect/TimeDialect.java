package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.thymeleaf.processor.*;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 *     时间相关方言
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
public class TimeDialect extends AbstractProcessorDialect {
    public static  String NAME = "Time";
    public static  String PREFIX = "time";
    public static  int PROCESSOR_PRECEDENCE = 2000;

    public TimeDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createArticleProcessorsSet(this,dialectPrefix);
    }

    private Set<IProcessor> createArticleProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new FormatProcessor(dialect,dialectPrefix));
        return processors;
    }
}
