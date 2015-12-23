/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 图库模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_gallery")
@Getter
@Setter
public class Gallery extends BaseEntity {

    private String title;//标题
    private String description;//描述
    @Lob
    private String content;//内容
    private String thumbUri;//缩略图Uri
    private String linkUrl;//链接地址
    @ManyToOne
    private Category category;//所属栏目
}
