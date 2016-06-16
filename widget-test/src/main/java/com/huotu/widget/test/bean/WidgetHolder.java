/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.widget.Widget;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@Component
public class WidgetHolder {

    private final Widget widget;

    public WidgetHolder() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("META-INF/widget.properties");
        try (InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        }
        assertThat(properties.getProperty("widgetClasses"))
                .isNotNull();

        widget = (Widget) Class.forName(properties.getProperty("widgetClasses")).newInstance();
    }

    public Widget getWidget() {
        return widget;
    }

}
