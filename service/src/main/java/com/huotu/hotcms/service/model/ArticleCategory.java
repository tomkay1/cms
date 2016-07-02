/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.model;

import com.huotu.hotcms.service.common.ArticleSource;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by chendeyu on 2016/1/4.
 */
@Setter
@Getter
public class ArticleCategory extends BaseModel {

    /**
     * 标题
     */
    private String title;

    /**
     * 缩略图
     */
    private String thumbUri;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章来源
     */
    private ArticleSource articleSource;

    /**
     * 文章内容（富文本）
     */
    private String content;

    /**
     * /浏览量
     */
    private int scans;

    /**
     * 点赞数量
     */
    private int lauds;

    /**
     * 被踩数量
     */
    private int unlauds;

//    /**
//     * 所属栏目
//     */
//    private Category category;

    /**
     * 所属栏目
     */
    private String categoryName;

    /**
     * 所属站点名称
     */
    private String siteName;

//    /**
//     * 所属站点
//     */
//    private Site site;
}
