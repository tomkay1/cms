/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.common;

/**
 * Created by Administrator on 2015/12/22.
 */
public enum ModelType implements CommonEnum {
    ARTICLE(0,"文章"),
    NOTICE(1,"公告"),
    VIDEO(2,"视频"),
    GALLERY(3,"图库"),
    DOWNLOAD(4,"下载"),
    LINK(5,"链接");

    ModelType(int code, String value) {
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

    public static ModelType valueOf(int value)
    {
        switch (value){
            case 0:
                return ARTICLE;
            case 1:
                return NOTICE;
            case 2:
                return  VIDEO;
            case 3:
                return GALLERY;
            case 4:
                return  DOWNLOAD;
            case 5:
                return LINK;
            default:
                return null;
        }
    }
}
