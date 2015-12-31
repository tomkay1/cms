package com.huotu.hotcms.web.common;

import org.thymeleaf.dialect.IDialect;

/**
 * <p>
 *     CMS 支持的方言
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public enum DialectTypeEnum  implements ICommonEnum {
    ARTICLE(0,"article"),
    DOWNLOAD(1,"download"),
    GALLERY(2,"gallery"),
    LINK(3,"link"),
    NOTICE(4,"notice"),
    CATEGORY(5,"category");

    DialectTypeEnum(int code, String value) {
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
