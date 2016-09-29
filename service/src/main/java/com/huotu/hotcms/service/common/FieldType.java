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
 * @author CJ
 */
public enum FieldType implements CommonEnum {

    /**
     * 具体保存的数据是{@link String}
     */
    Text(0, "文本"),
    /**
     * 比如价格之类以货币为单位的结算数量
     */
    Money(1, "金额"),
    /**
     * 以png格式保存的图片资源
     *
     * @see me.jiangcai.lib.resource.service.ResourceService
     */
    Image(2, "图片"),
    /**
     * 跟{@link #Text}不同的是它支持部分html语法
     */
    RichText(3, "富文本");

    /**
     * 商城类目
     */
//    MallCategory(4, "商城类目");

    private final int code;
    private final String value;
    private final String description;

    FieldType(int code, String value) {
        this(code, value, value);
    }

    FieldType(int code, String value, String description) {
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
