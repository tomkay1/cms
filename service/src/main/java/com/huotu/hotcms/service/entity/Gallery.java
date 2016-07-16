/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 图库模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_gallery")
@Getter
@Setter
public class Gallery extends AbstractContent {


    /**
     * 内容
     */
    @Lob
    @Column(name = "content")
    private String content;

    /**
     * 缩略图Uri
     */
    @Column(name = "thumbUri")
    private String thumbUri;

    /**
     * 链接地址
     */
    @Column(name = "linkUrl")
    private String linkUrl;

    @Override
    public Gallery copy() {
        Gallery gallery=new Gallery();
        gallery.setContent(content);
        gallery.setThumbUri(thumbUri);
        gallery.setOrderWeight(getOrderWeight());
        gallery.setTitle(getTitle());
        gallery.setSerial(getSerial());
        gallery.setCategory(getCategory());
        gallery.setDeleted(isDeleted());
        gallery.setLinkUrl(linkUrl);
        gallery.setCreateTime(LocalDateTime.now());
        gallery.setUpdateTime(LocalDateTime.now());
        gallery.setDescription(getDescription());
        return gallery;
    }

//    /**
//     * 所属栏目
//     */
//    @Basic
//    @ManyToOne
//    @JoinColumn(name = "categoryId")
//    private Category category;
}
