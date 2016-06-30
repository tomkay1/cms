/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.converter;

import com.huotu.hotcms.service.entity.Category;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author CJ
 */
@Component
public class CategoryFormatter extends EntityFormatter<Category, Long> {

    @Override
    public String print(Category object, Locale locale) {
        return null;
    }
}
