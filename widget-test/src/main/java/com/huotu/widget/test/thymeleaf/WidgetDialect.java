/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import com.huotu.widget.test.thymeleaf.process.ReplaceEditorProcessor;
import org.springframework.stereotype.Service;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.HashSet;
import java.util.Set;

/**
 * 模拟widget-service中向控件服务提供的前端方言服务
 *
 * @author CJ
 */
@Service
public class WidgetDialect extends AbstractDialect implements IProcessorDialect {

    @Autowired
    private Set<WidgetProcessor> widgetProcessors;

    public WidgetDialect() {
        super("Widget");
    }

    @Override
    public String getPrefix() {
        return "w";
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 10000;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
      
        HashSet<IProcessor> iProcessors = new HashSet<>();
        iProcessors.addAll(widgetProcessors);
        return Collections.unmodifiableSet(iProcessors);
    }
}
