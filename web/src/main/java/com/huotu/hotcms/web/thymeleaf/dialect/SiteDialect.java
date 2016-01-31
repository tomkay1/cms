package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.thymeleaf.processor.CurrentProcessor;
import com.huotu.hotcms.web.thymeleaf.processor.ForeachProcessor;
import com.huotu.hotcms.web.thymeleaf.processor.HrefProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/1/5.
 */
public class SiteDialect extends AbstractProcessorDialect {
    public static  String NAME = "Site";
    public static  String PREFIX = "site";
    public static  int PROCESSOR_PRECEDENCE = 2000;

    public SiteDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createArticleProcessorsSet(this,dialectPrefix);
    }

    private Set<IProcessor> createArticleProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new ForeachProcessor(dialect, dialectPrefix));
        processors.add(new CurrentProcessor(dialect, dialectPrefix));
//        processors.add(new HrefProcessor(dialect, dialectPrefix));
        return processors;
    }
}
