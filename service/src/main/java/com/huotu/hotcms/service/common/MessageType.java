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
public enum MessageType implements CommonEnum {

    GOODS(0,"商品回复"),
    ARTICLE(1,"文章回复"),
    GALLERY(2,"图库回复"),
    NOTICE(3,"公告回复"),
    VIDEO(4,"视频回复"),
    DOWNLOAD(4,"下载回复"),
    WIDGET(5,"装修模板回复");


    private int code;
    private String value;
    MessageType(int code, String value) {
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
