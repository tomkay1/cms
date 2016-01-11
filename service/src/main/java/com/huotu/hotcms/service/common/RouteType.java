package com.huotu.hotcms.service.common;

import com.huotu.hotcms.service.entity.Route;

import java.util.Map;

/**
 * Created by Administrator xhl 2016/1/7.
 */
public enum RouteType implements CommonEnum{
    NOT_FOUND(0,"404页面"),
    SERVER_ERROR(1,"服务器错误页面"),
    ARTICLE_CONTENT(2,"文章内容页面"),
    VIDEO_CONTENT(3,"视频内容页面"),
    GALLERY_CONTENT(4,"图片内容页面"),
    ARTICLE_LIST(5,"文章列表页面");

    RouteType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public static RouteType valueOf(int value)
    {
        switch (value){
            case 2:
                return ARTICLE_CONTENT;
            case 0:
                return NOT_FOUND;
            case 1:
                return SERVER_ERROR;
            default:
                return null;
        }
    }

    public static RouteType[] ConvertMapToEnum(){
        RouteType[] routeTypes=RouteType.values();
        return routeTypes;
    }


}
