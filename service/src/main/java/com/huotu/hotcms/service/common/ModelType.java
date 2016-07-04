/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.common;

/**
 * Created by xhl on 2015/12/22.
 */
public enum ModelType implements CommonEnum {
    ARTICLE(0,"文章模型"),
    NOTICE(1,"公告模型"),
    VIDEO(2,"视频模型"),
    GALLERY(3,"图库模型"),
    DOWNLOAD(4,"下载模型"),
    LINK(5,"链接模型");
//    PRODUCT(6, "产品模型"),
//    LOGO(7, "LOGO模型");

    private int code;
    private String value;
    ModelType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
