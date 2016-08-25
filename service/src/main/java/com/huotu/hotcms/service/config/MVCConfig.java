package com.huotu.hotcms.service.config;

import com.huotu.hotcms.service.converter.CommonEnumConverter;
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
    private Set<CommonEnumConverter> commonEnumConverterSet;

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
