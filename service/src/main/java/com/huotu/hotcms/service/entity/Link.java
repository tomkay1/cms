/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 链接模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_link")
@Getter
@Setter
public class Link extends BaseModel {
//
//    /**
//     * 标题
//     */
//    @Column(name = "title")
//    private String title;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 缩略图uri
     */
    @Column(name = "thumbUri")
    private String thumbUri;

    /**
     * 链接地址
     */
    @Column(name = "linkUrl")
    private String linkUrl;

    /**
     * 所属栏目
     */
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}
