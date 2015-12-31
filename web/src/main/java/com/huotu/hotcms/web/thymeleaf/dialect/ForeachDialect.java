package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.thymeleaf.processor.ForeachProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @brief 自定义循环thymeleaf 语法标签
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
public class ForeachDialect extends AbstractProcessorDialect {
    public static final String NAME = "huotu";
    public static final String PREFIX = "hot";
    public static final int PROCESSOR_PRECEDENCE = 800;

    public ForeachDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createHotProcessorsSet(this,dialectPrefix);
    }

    public static Set<IProcessor> createHotProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect, TemplateMode.HTML, dialectPrefix));
        return processors;
    }
}
