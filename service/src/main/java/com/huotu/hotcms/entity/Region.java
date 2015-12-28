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
     * 地区编号（cn,us,etc.）
     */
    @Column(name = "regionCode")
    private String regionCode;

    /**
     * 地区名称
     */
    @Column(name = "regionName")
    private String regionName;

    /**
     * 语言编号（zh,en,etc.）
     */
    @Column(name = "langCode")
    private String langCode;

    /**
     * 语言名称
     */
    @Column(name = "langName")
    private String langName;

    /**
     * 语言-地区代码(zh-cn,zh-tw,en-us,etc.)
     */
    @Column(name = "langTag")
    private String langTag;


}
