/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.config;

import com.huotu.hotcms.bean.WidgetHolder;
import com.huotu.hotcms.hold.AbstractWidgetHolder;
import com.huotu.hotcms.widget.Widget;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * preview使用的配置
 */
@Configuration
@ComponentScan("com.huotu.hotcms.controller")
public class PreviewConfig extends WidgetTestConfig {

    /**
     * classPath告诉我
     */
    public static File classesPath;

    @Bean
    @DependsOn("widgetPreviewDialect")
    public WidgetHolder widgetHolder() throws ClassNotFoundException, InstantiationException, IllegalAccessException
            , IOException {
        assert classesPath != null;
        File propertiesFile = new File(classesPath, "META-INF/widget.properties");
        URLClassLoader classLoader = new URLClassLoader(new URL[]{classesPath.toURI().toURL()}
                , Thread.currentThread().getContextClassLoader());
        return new AbstractWidgetHolder(new FileSystemResource(propertiesFile)
                , className -> (Widget) classLoader.loadClass(className).newInstance());
    }

}
