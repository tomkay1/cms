package com.huotu.hotcms.util;

import com.huotu.hotcms.common.CommonEnum;

/**
 * @since 1.0.0
 * @author xhl
 * @time 2015/15/25
 */
public enum ResultOptionEnum implements CommonEnum {
    OK(200,"成功"),
    NOLOGIN(201,"没有登录"),
    NO_LIMITS(202,"没有权限"),
    DOMIN_EXIST(203,"域名已经存在"),

    NOFIND(404,"没有信息"),
    FAILE(500,"失败"),
    PARAMERROR(501,"参数错误"),
    SERVERFAILE(502,"服务器错误");

    private int code;
    private String value;

    ResultOptionEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

}
