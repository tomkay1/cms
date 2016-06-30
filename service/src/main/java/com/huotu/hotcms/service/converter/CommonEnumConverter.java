/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.converter;

import com.huotu.hotcms.service.common.CommonEnum;
import com.huotu.hotcms.service.common.EnumUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @author CJ
 */
public abstract class CommonEnumConverter<T extends CommonEnum> implements Converter<String, T> {

    private final Class<T> enumClass;

    public CommonEnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T convert(String source) {
        // 根据范型获取类型 算了麻烦 初始化过来吧
        return EnumUtils.valueOf(enumClass, source);
    }
}
