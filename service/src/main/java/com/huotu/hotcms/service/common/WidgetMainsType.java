package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2016/4/14.
 */
public enum WidgetMainsType implements CommonEnum  {
    widget_common(0,"公共"),
    pc_website(1,"PC官网"),
    pc_shop(2,"PC商城");

    WidgetMainsType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;


    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
