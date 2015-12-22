/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import com.huotu.hotcms.common.ArticleSource;
import com.huotu.hotcms.common.ModelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 文章模型
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_article")
@Getter
@Setter
public class Article extends DataEntity {

    private String title;//标题
    private String thumbUri;//缩略图
    private String description;//描述
    private String author;//作者
    private ArticleSource articleSource;//文章来源
    @Lob
    private String content;//文章内容（富文本）
    private int scans;//浏览量
    private int lauds;//点赞数量
    private int unlauds;//被踩数量

}
