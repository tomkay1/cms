/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.common;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 排序依据
 * Created by cwb on 2016/3/17.
 */
public enum SortEnum implements CommonEnum{
    SALES(0,"销量"),
    PRICE(1,"价格"),
    SHELVE_TIME(2,"上架时间");
    private int code;
    private String value;

    SortEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    @JsonValue
    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
