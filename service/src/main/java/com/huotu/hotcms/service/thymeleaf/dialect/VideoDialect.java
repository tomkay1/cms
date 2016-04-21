/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.thymeleaf.processor.CurrentProcessor;
import com.huotu.hotcms.service.thymeleaf.processor.ForeachProcessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by cwb on 2016/1/15.
 */
@Component
public class VideoDialect extends AbstractProcessorDialect {
    public static  String NAME = "Video";
    public static  String PREFIX = "video";
    public static  int PROCESSOR_PRECEDENCE = 800;

    public VideoDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createArticleProcessorsSet(this,dialectPrefix);
    }

    private Set<IProcessor> createArticleProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new ForeachProcessor(dialect,dialectPrefix));
        processors.add(new CurrentProcessor(dialect,dialectPrefix));
        return processors;
    }
}
