package com.huotu.hotcms.service.common;

/**
 * <p>
 *    网站装修类型
 * </p>
 */
public enum SiteType implements CommonEnum {
    SITE_PC_WEBSITE(0,"PC官网"),
    SITE_PC_SHOP(1,"PC商城");

    SiteType(int code, String value) {
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

    public static SiteType valueOf(int value)
    {
        switch (value){
            case 0:
                return SITE_PC_WEBSITE;
            case 1:
                return SITE_PC_SHOP;
            default:
                return null;
        }
    }

//    public static SiteType[] ConvertMapToEnum(){
//        SiteType[] routeTypes=SiteType.values();
//        return routeTypes;
//    }
}
