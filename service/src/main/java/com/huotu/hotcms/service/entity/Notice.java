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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 公告模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_notice")
@Setter
@Getter
public class Notice extends BaseEntity {

//    /**
//     * 公告标题
//     */
//    private String title;

    /**
     * 公告内容
     */
    private String content;

//    /**
//     * 所属栏目
//     */
//    @ManyToOne
//    private Category category;

}
