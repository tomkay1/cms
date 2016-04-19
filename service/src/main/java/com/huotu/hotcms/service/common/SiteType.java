package com.huotu.hotcms.service.common;

/**
 * <p>
 *    网站装修类型
 * </p>
 */
public enum SiteType implements CommonEnum {
    SITE_PC_WEBSITE(0,"PC官网"),
    SITE_PC_SHOP(1,"PC商城");

    private int code;
    private String value;
    SiteType(int code, String value) {
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

//    public static SiteType[] ConvertMapToEnum(){
//        SiteType[] routeTypes=SiteType.values();
//        return routeTypes;
//    }
}
