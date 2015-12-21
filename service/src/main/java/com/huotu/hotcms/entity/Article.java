/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import com.huotu.hotcms.common.ArticleSource;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/12/21.
 */
@Entity
@Table(name = "cms_article")
@Setter
@Getter
public class Article extends DataModel {

    private String title;
    private String thumbUri;//缩略图
    private String description;
    private String author;
    private ArticleSource articleSource;
    private String content;
    private int scans;//浏览量
    private int lauds;//点赞数量
    private int unlauds;//被踩数量

}
