/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.config;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author CJ
 */
public abstract class SimpleInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static WebApplicationContext webApplicationContext;

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        if (webApplicationContext == null) {
            webApplicationContext = super.createRootApplicationContext();
        }
        return webApplicationContext;
    }

    @Override
    protected final Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }
}
