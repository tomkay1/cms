/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.thymeleaf.processor.GoodsPageableTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 商城组件解析器
 * Created by cwb on 2016/3/17.
 */
public class WidgetDialect extends AbstractProcessorDialect {

    public static String NAME = "widget";
    public static String PREFIX = "widget";
    public static  int PROCESSOR_PRECEDENCE = 195;

    public WidgetDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createWidgetProcessorsSet(this, dialectPrefix);
    }

    private Set<IProcessor> createWidgetProcessorsSet(final IProcessorDialect dialect, String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new GoodsPageableTagProcessor(dialect,dialectPrefix));
        return processors;
    }

}
