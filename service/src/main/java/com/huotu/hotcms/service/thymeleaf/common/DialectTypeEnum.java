/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.common;

/**
 * <p>
 *     CMS 支持的方言
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public enum DialectTypeEnum {
    ARTICLE("article", ParamEnum.ARTICLE),
    LINK("link",ParamEnum.LINK),
    SITE("site",ParamEnum.SITE),
    DOWNLOAD("download",ParamEnum.DOWNLOAD),
    GALLERY("gallery",ParamEnum.GALLERY),
    GALLERYLIST("galleryList",ParamEnum.GALLERYLIST),
    NOTICE("notice",ParamEnum.NOTICE),
    CATEGORY("category",ParamEnum.CATEGORY),
    VIDEO("video",ParamEnum.VIDEO),
    TIME("time",ParamEnum.TIME);
    private String dialectPrefix;
    private Object  params;

    DialectTypeEnum(String dialectPrefix, Object params) {
        this.dialectPrefix = dialectPrefix;
        this.params = params;
    }

    public String getDialectPrefix() {
        return dialectPrefix;
    }

    public static ParamEnum getDialectParam(String dialectPrefix) {
        switch (dialectPrefix) {
            case "article":
                return ParamEnum.ARTICLE;
            case "link":
                return ParamEnum.LINK;
            case "site":
                return ParamEnum.SITE;
            case "category":
                return ParamEnum.CATEGORY;
            case "download":
                return ParamEnum.DOWNLOAD;
            case "gallery":
                return ParamEnum.GALLERY;
            case "galleryList":
                return ParamEnum.GALLERYLIST;
            case "notice":
                return ParamEnum.NOTICE;
            case "time":
                return ParamEnum.TIME;
        }
        return null;
    }

    public void setDialectPrefix(String dialectPrefix) {
        this.dialectPrefix = dialectPrefix;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
