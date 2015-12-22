/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.common;

/**
 * Created by Administrator on 2015/12/22.
 */
public enum ModelType implements CommonEnum {
    ARTICLE(0,"文章"),
    NOTICE(1,"公告"),
    VIDEO(2,"视频"),
    GALLERY(3,"图库"),
    DOWNLOAD(4,"下载"),
    LINK(5,"链接"),
    CUSTOM(6,"自定义");

    ModelType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
