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
    DATA_HOT_ID(0,"data-hot-id"),
    DATA_HOT_IgnoreID(1,"data-hot-ignoreId"),
    DATA_HOT_SIZE(2,"data-hot-size"),
    DATA_HOT_SITEID(3,"data-hot-siteId"),
    DATA_HOT_CUSTOMERID(4,"data-hot-customerId"),
    DATA_HOT_CATEGORYID(5,"data-hot-categoryId"),
    DATA_HOT_DATATYPE(6,"data-hot-type");

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
