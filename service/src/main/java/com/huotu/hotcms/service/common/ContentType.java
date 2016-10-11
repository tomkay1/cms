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
 * 正文类型
 *
 * @author CJ
 */
public enum ContentType implements CommonEnum {
    /**
     *
     */
    Article(0, "文章", "富文本内容"),
    /**
     *
     */
    Link(1, "链接", "链接内容"),
    /**
     *
     */
    Video(2, "视频", "视频内容"),
    /**
     *
     */
    Notice(3, "公告", "类似文章内容"),
    /**
     *
     */
    Gallery(4, "图库", "图片内容"),
    /**
     *
     */
    Download(5, "下载", "任意提供下载的内容"),
    /**
     *
     */
    Page(6, "页面", "用户自定义页面"),

    /**
     *
     */
    Product(7, "产品", "产品内容"),

    /**
     *
     */
    MallProduct(8, "商城产品数据源", "产品数据源"),

    /**
     *
     */
    MallClass(9, "商城类目数据源", "产品数据源");

    private final int code;
    private final String value;
    private final String description;

    ContentType(int code, String value, String description) {
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

    /**
     * @return 是否为产品类型
     */
    public boolean isProduct() {
        return this == Product;
    }

    public boolean isPage() {
        return this == Page;
    }

    /**
     * @return 普通的内容类型, 没有一些特殊约定的
     */
    public boolean isNormal() {
        return !isProduct() && !isPage();
    }
}
