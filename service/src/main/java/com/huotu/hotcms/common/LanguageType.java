/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.common;

/**
 * 语言
 * Created by cwb on 2015/12/23.
 */
public enum LanguageType implements CommonEnum {
    SIMPLIFIED_CHINESE(0,"zh-CN"),
    ENGLISH(1,"en");

    private int code;
    private String value;

    LanguageType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
