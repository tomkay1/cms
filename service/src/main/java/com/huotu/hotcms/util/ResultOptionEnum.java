package com.huotu.hotcms.util;

import com.huotu.hotcms.common.CommonEnum;

/**
 * Created by Administrator on 2015/12/25.
 */
public enum ResultOptionEnum implements CommonEnum {
    OK(200,"成功"),
    NOLOGIN(201,"没有登录"),
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
