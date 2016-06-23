/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

/**
 * @author CJ
 */
@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale val) {
        StringBuilder stringBuilder = new StringBuilder(val.getLanguage());
        if (val.getCountry().length() > 0) {
            stringBuilder.append("_").append(val.getCountry());
            if (val.getVariant().length() > 0)
                stringBuilder.append("_").append(val.getVariant());
        }
        return stringBuilder.toString();
    }

    @Override
    public Locale convertToEntityAttribute(String str) {
        if (StringUtils.isEmpty(str))
            return null;
        String[] params = str.split("_", 3);
        if (params.length == 0)
            return null;

        String language = params[0];
        String country = "";
        if (params.length > 1)
            country = params[1];
        String variant = "";
        if (params.length > 2)
            variant = params[2];

        return new Locale(language, country, variant);
    }
}