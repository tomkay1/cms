/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.Formatter;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author CJ
 */
public abstract class EntityFormatter<T, ID extends Serializable> implements Formatter<T> {
    @Autowired
    private JpaRepository<T, ID> jpaRepository;
    @Autowired(required = false)
    private ConversionService conversionService;

    @Override
    public T parse(String text, Locale locale) throws ParseException {
        if (text == null)
            return null;
        ParameterizedType idParameterizedType = (ParameterizedType) getClass().getGenericInterfaces()[1];
        @SuppressWarnings("unchecked") Class<ID> clazz = (Class<ID>) idParameterizedType.getRawType();

        ID id = conversionService.convert(text, clazz);
        return jpaRepository.getOne(id);
    }

}
