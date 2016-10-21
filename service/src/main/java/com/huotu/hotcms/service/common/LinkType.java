package com.huotu.hotcms.service.common;

/**
 * Created by lhx on 2016/10/18.
 */
public enum LinkType implements CommonEnum {
    /**
     *
     */
    Link(0, "外链", "外链接地址"),
    /**
     *
     */
    page(1, "页面", "站内页面");

    private final int code;
    private final String value;
    private final String description;

    LinkType(int code, String value, String description) {
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

    /**
     * @return 是否为页面类型
     */
    public boolean isPage() {
        return this == page;
    }


}
