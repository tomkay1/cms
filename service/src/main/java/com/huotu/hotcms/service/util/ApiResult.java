/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.util;

/**
 * 接口返回实体
 * Created by liual on 2015-09-21.
 */
public class ApiResult<T> {

    /**
     * 返回结果描述
     */
    private String msg;

    private int code;

    /**
     * 返回数据
     */
    private T data;

    public ApiResult() {
        super();
    }

    public ApiResult(String msg,int code) {
        this.msg = msg;
        this.code = code;
    }

    public ApiResult(T obj) {
        this.data = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
