package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2016/4/14.
 */
public enum ScopesType implements CommonEnum {
    COMMON(0,"公共"),
    PC_WEBSITE(1,"PC官网"),
    PC_SHOP(2,"PC商城");

    ScopesType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public static ScopesType valueOf(int value)
    {
        switch (value){
            case 0:
                return COMMON;
            case 1:
                return PC_WEBSITE;
            case 2:
                return  PC_SHOP;
            default:
                return null;
        }
    }

    public static ScopesType[] ConvertMapToEnum(){
        ScopesType[] routeTypes=ScopesType.values();
        return routeTypes;
    }
}
