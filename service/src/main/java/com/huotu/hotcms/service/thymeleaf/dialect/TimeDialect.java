/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.thymeleaf.processor.FormatProcessor;
import com.huotu.hotcms.service.thymeleaf.service.FormatProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class TimeDialect extends AbstractProcessorDialect {
    public static  String NAME = "Time";
    public static  String PREFIX = "time";
    public static  int PROCESSOR_PRECEDENCE = 800;

    @Autowired
    private FormatProcessorService formatProcessorService;

    public TimeDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createArticleProcessorsSet(this,dialectPrefix);
    }

    private Set<IProcessor> createArticleProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new FormatProcessor(dialect, dialectPrefix, formatProcessorService));
        return processors;
    }
}
