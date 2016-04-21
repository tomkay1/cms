package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.thymeleaf.processor.ForeachProcessor;
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
public class GalleryListDialect extends AbstractProcessorDialect{

    public static  String NAME = "GalleryList";
    public static  String PREFIX = "galleryList";
    public static  int PROCESSOR_PRECEDENCE = 800;

    public GalleryListDialect() {
        super(NAME,PREFIX,PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createGalleryListProcessorSet(this, dialectPrefix);
    }

    private Set<IProcessor> createGalleryListProcessorSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect,dialectPrefix));
        return processors;
    }
}
