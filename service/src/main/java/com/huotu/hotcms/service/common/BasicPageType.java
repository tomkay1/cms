package com.huotu.hotcms.service.common;

/**
 * <P>
 *     页面基础配置枚举
 * </P>
 *
 * @since 1.2
 */
public enum BasicPageType implements CommonEnum {
    PAGE_BASE_CONFIG(0,"base.xml"),
    PAGE_HEAD_CONFIG(1,"head.xml"),
    PAGE_HOME_CONFIG(2,"index.xml"),
    PAGE_LIST_CONFIG(3,"list.xml"),
    PAGE_SEARCH_CONFIG(4,"search.xml"),
    PAGE_BOTTOM_CONFIG(5,"bottom.xml");

    BasicPageType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
