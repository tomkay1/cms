package com.huotu.hotcms.web.common;

/**
 * <p>
 *     Thymeleaf 扩展标签枚举
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public enum DialectAttrNameEnum implements ICommonEnum {
    FOREACH(0,"foreach");

    DialectAttrNameEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    @Override
    public Object getCode() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
