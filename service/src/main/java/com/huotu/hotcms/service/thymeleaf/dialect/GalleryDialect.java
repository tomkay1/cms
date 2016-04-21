package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.thymeleaf.processor.ForeachProcessor;
import com.huotu.hotcms.service.thymeleaf.service.ForeachProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by chendeyu on 2016/3/23.
 */
@Component
public class GalleryDialect  extends AbstractProcessorDialect{

    public static  String NAME = "Gallery";
    public static  String PREFIX = "gallery";
    public static  int PROCESSOR_PRECEDENCE = 800;
    @Autowired
    private ForeachProcessorService foreachProcessorService;

    public GalleryDialect() {
        super(NAME,PREFIX,PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createGalleryProcessorSet(this, dialectPrefix);
    }

    private Set<IProcessor> createGalleryProcessorSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect, dialectPrefix, foreachProcessorService));
        return processors;
    }
}
