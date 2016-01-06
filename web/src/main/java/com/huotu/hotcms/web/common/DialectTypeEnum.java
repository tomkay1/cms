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

    public void setDialectPrefix(String dialectPrefix) {
        this.dialectPrefix = dialectPrefix;
    }

    public Object getParams() {
        return params;
    }

    public Object getParams(String dialectPrefix) {
        Object object = null;
        switch (dialectPrefix) {
            case "article":
                object = ParamEnum.ARTICLE;
            break;
            case "link":
                object = ParamEnum.LINK;
            break;
        }
        return object;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
