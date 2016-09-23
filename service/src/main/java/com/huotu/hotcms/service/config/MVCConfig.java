/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.config;

import com.huotu.hotcms.service.converter.AutowireConverter;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Set;

@Configuration
@EnableWebMvc
public class MVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Set<AutowireConverter> commonEnumConverterSet;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        commonEnumConverterSet.forEach(registry::addConverter);
        registry.addConverter(new Converter<String, WidgetIdentifier>() {
            @Override
            public WidgetIdentifier convert(String source) {
                if (source == null)
                    return null;
                return WidgetIdentifier.valueOf(source);
            }
        });
    }


}
