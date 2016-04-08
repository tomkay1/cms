/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author CJ
 */
public class StringWithCommaSetConverter extends StdConverter<String, Set<String>> {
    @SuppressWarnings("unchecked")
    @Override
    public Set<String> convert(String value) {
        if (value == null)
            return null;
        if (value.trim().length() == 0)
            return null;
        if (value.trim().equalsIgnoreCase("null"))
            return null;
        return new HashSet<>(Arrays.asList(value.split(",")));
    }
}
