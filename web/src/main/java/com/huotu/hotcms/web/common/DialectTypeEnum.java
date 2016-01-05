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
    LINK("link",ParamEnum.LINK);
//    DOWNLOAD(1,"download"),
//    GALLERY(2,"gallery"),
//    LINK(3,"link"),
//    NOTICE(4,"notice"),
//    CATEGORY(5,"category"),
//    SITE(6,"site");
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
