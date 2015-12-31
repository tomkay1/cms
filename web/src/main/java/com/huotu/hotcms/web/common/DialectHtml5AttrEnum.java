package com.huotu.hotcms.web.common;

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
