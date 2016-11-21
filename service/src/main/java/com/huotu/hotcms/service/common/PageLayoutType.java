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
 * 页面布局
 *
 * @author CJ
 */
public enum PageLayoutType implements CommonEnum {
    /**
     * 响应
     */
    responsive(0, "响应式", "适用pc官网"),
    /**
     * 传统(固定)
     */
    traditional(1, "传统(固定)", "适用pc商城");

    private final int code;
    private final String value;
    private final String description;

    PageLayoutType(int code, String value, String description) {
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
}
