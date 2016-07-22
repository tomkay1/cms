/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.widget.page.PageLayout;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * @author CJ
 */
@Converter
public class PageLayoutConverter implements AttributeConverter<PageLayout, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PageLayout pageLayout) {
        if (pageLayout == null)
            return null;
        try {
            return objectMapper.writeValueAsString(pageLayout);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("can not make string from PageLayout", e);
        }
    }

    @Override
    public PageLayout convertToEntityAttribute(String s) {
        if (StringUtils.isEmpty(s))
            return null;
        try {
            return objectMapper.readValue(s, PageLayout.class);
        } catch (IOException e) {
            throw new IllegalStateException("can not resole page xml:" + s, e);
        }
    }
}
