package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.service.BaseDialectService;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 *     自定义循环thymeleaf 语法标签基类
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class BaseDialect  extends AbstractProcessorDialect {
    public static final String NAME = "huotu";
    public static final String PREFIX = "hot";
    public static final int PROCESSOR_PRECEDENCE = 800;
    private static BaseDialectService baseDialectService;

    public BaseDialect(BaseDialectService baseDialectService) {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
        this.baseDialectService=baseDialectService;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createHotProcessorsSet(this,dialectPrefix);
    }

    public static Set<IProcessor> createHotProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new BaseProcessor(dialect, TemplateMode.HTML, dialectPrefix,baseDialectService));
        return processors;
    }
}
