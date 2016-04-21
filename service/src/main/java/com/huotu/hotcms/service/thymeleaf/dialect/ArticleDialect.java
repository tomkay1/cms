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
import com.huotu.hotcms.service.thymeleaf.processor.NextProcessor;
import com.huotu.hotcms.service.thymeleaf.processor.PreviousProcessor;
import com.huotu.hotcms.service.thymeleaf.service.CurrentProcessorService;
import com.huotu.hotcms.service.thymeleaf.service.ForeachProcessorService;
import com.huotu.hotcms.service.thymeleaf.service.NextProcessorService;
import com.huotu.hotcms.service.thymeleaf.service.PreviousProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by cwb on 2016/1/4.
 */
@Component
public class ArticleDialect extends AbstractProcessorDialect {

    public static String NAME = "Article";
    public static String PREFIX = "article";
    public static int PROCESSOR_PRECEDENCE = 2000;
    @Autowired
    private ForeachProcessorService foreachProcessorService;
    @Autowired
    private CurrentProcessorService currentProcessorService;
    @Autowired
    private NextProcessorService nextProcessorService;
    @Autowired
    private PreviousProcessorService previousProcessorService;

    public ArticleDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createArticleProcessorsSet(this, dialectPrefix);
    }


    private Set<IProcessor> createArticleProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect, dialectPrefix, foreachProcessorService));
        processors.add(new CurrentProcessor(dialect, dialectPrefix, currentProcessorService));
        processors.add(new NextProcessor(dialect, dialectPrefix, nextProcessorService));
        processors.add(new PreviousProcessor(dialect, dialectPrefix, previousProcessorService));
//        processors.add(new HrefProcessor(dialect, dialectPrefix));
//        processors.add(new SrcProcessor(dialect, dialectPrefix));
        return processors;
    }

}
