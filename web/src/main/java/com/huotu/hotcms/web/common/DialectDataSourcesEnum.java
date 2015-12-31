package com.huotu.hotcms.web.common;

/**
 * @brief Thymeleaf Html5 数据源枚举
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
public enum DialectDataSourcesEnum implements ICommonEnum{
    DATA_SOURCES_ARTICLE(0,"article"),
    DATA_SOURCES_DOWNLOAD(1,"download"),
    DATA_SOURCES_GALLERY(2,"gallery"),
    DATA_SOURCES_LINK(3,"link"),
    DATA_SOURCES_NOTICE(4,"notice"),
    DATA_SOURCES_CATEGORY(5,"category");

    DialectDataSourcesEnum(int code, String value) {
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
