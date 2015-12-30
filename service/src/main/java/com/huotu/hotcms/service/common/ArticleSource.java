/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2015/12/21.
 */
public enum ArticleSource implements CommonEnum {
    REPRINT(0,"转载"),
    ORIGINAL(1,"原创");

    ArticleSource(int code, String value) {
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
