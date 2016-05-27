package com.huotu.hotcms.service.common;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public enum RouteType implements CommonEnum{
    NOT_FOUND(0,"404页面"),
    SERVER_ERROR(1,"服务器错误页面"),
    ARTICLE_CONTENT(2,"文章内容页面"),
    VIDEO_CONTENT(3,"视频内容页面"),
    GALLERY_CONTENT(4,"图片内容页面"),
    ARTICLE_LIST(5,"文章列表页面"),
    HEADER_NAVIGATION(6,"网站导航栏目"),
    VIDEO_LIST(7,"视频列表页面"),
    NO_CONFIG(8,"暂时没有设置");


    private int code;
    private String value;
    RouteType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
