/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.common;



/**
 * Created by Administrator on 2016/1/22.
 */
public enum AuditStatus implements CommonEnum {

    APPLYING(0,"申请中"),
    PROCESSED(1,"通过"),
    FAIL(2,"失败");


    private int code;
    private String value;
    AuditStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
