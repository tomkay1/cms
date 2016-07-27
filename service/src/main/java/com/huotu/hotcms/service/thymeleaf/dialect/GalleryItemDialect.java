/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

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
public class GalleryItemDialect extends AbstractProcessorDialect{

    public static String NAME = "GalleryItem";
    public static  String PREFIX = "galleryList";
    public static  int PROCESSOR_PRECEDENCE = 800;
    @Autowired
    private ForeachProcessorService foreachProcessorService;

    public GalleryItemDialect() {
        super(NAME,PREFIX,PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createGalleryItemProcessorSet(this, dialectPrefix);
    }

    private Set<IProcessor> createGalleryItemProcessorSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect, dialectPrefix, foreachProcessorService));
        return processors;
    }
}
