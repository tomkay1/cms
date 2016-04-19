package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2016/4/14.
 */
public enum ScopesType implements CommonEnum {
    COMMON(0,"公共"),
    PC_WEBSITE(1,"PC官网"),
    PC_SHOP(2,"PC商城");

    private int code;
    private String value;
    ScopesType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
