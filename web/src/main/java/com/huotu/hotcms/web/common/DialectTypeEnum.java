package com.huotu.hotcms.web.common;

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
    NOTICE("notice",ParamEnum.NOTICE),
    CATEGORY("category",ParamEnum.CATEGORY);
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
            case "notice":
                return ParamEnum.NOTICE;
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
