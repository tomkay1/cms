/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

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

    @Override
    public String toString() {
        return value;
    }


//    public static SiteType[] ConvertMapToEnum(){
//        SiteType[] routeTypes=SiteType.values();
//        return routeTypes;
//    }
}
