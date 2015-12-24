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

import javax.persistence.*;

/**
 * 地区/国家
 * Created by cwb on 2015/12/24.
 */
@Entity
@Table(name = "cms_region")
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地区编号（cn,en,etc.）
     */
    private String region;

    /**
     * 通行语言
     */
    @ManyToOne
    private Language language;

    /**
     * 备注
     */
    private String remarks;

}
