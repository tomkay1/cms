/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.loader.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * CMS提供的方言
 *
 * @author CJ
 */
@Service
public class CMSDialect extends AbstractDialect implements IProcessorDialect {

    public static final String Prefix = "cms";

    @Autowired
    private Set<CMSProcessor> CMSProcessors;

    public CMSDialect() {
        super("CMS");
    }

    @Override
    public String getPrefix() {
        return Prefix;
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 10000;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
      
        HashSet<IProcessor> iProcessors = new HashSet<>();
        iProcessors.addAll(CMSProcessors);
        iProcessors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return Collections.unmodifiableSet(iProcessors);
    }
}
