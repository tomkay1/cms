package com.huotu.hotcms.service.common;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public enum RouteType implements CommonEnum{
    NOT_FOUND(404,"404页面"),
    SERVER_ERROR(500,"服务器错误页面"),
    ARTICLEDETILE(2,"文章内容页面");

    RouteType(int code, String value) {
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
