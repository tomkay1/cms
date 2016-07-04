/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.common;

import com.huotu.hotcms.service.common.CommonEnum;

/**
 * Created by lhx on 2016/7/4.
 */
public enum PageType implements CommonEnum {
    DataIndex(1, "数据索引", "是一个数据列表"), DataContent(2, "数据内容", "是一个内容页"), Ordinary(3, "普通", "普通页面");

    private final int code;
    private final String value;
    private final String description;

    PageType(int code, String value, String description) {
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
