package com.huotu.hotcms.service.common;

/**
 * <p>
 * 网站装修类型
 * </p>
 */
public enum SiteType implements CommonEnum {
    SITE_PC_WEBSITE(0, "PC官网", ""),
    SITE_PC_SHOP(1, "PC商城", "");

    private final int code;
    private final String value;
    private final String description;

    SiteType(int code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }


//    public static SiteType[] ConvertMapToEnum(){
//        SiteType[] routeTypes=SiteType.values();
//        return routeTypes;
//    }
}
