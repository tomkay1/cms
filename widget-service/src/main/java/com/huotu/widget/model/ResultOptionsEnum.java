package com.huotu.widget.model;

/**
 * Created by hzbc on 2016/5/27.
 */

/**
 * 请求接口响应信息
 */
public enum ResultOptionsEnum {
    SUCCESS(202,"成功"),
    FAIL(500,"失败");

    private int code;
    private String value;

    ResultOptionsEnum(int code, String value){
        this.code=code;
        this.value=value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
