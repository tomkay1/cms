/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.common;

/**
 * <p>
 *     Thymeleaf Html5 Attribute enum
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public enum DialectHtml5AttrEnum implements ICommonEnum {
    DATA_PARAM_ID(0,"data-param-id"),
    DATA_PARAM_EXCLUDEID(1,"data-param-excludeId"),
    DATA_PARAM_SIZE(2,"data-param-size"),
    DATA_PARAM_SITEID(3,"data-param-siteId"),
    DATA_PARAM_CUSTOMERID(4,"data-param-customerId"),
    DATA_PARAM_CATEGORYID(5,"data-param-categoryId"),
    DATA_PARAM_DATATYPE(6,"data-param-type");

    DialectHtml5AttrEnum(int code, String value) {
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
