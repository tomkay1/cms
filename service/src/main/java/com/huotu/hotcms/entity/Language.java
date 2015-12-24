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
 * 语言
 * Created by cwb on 2015/12/24.
 */
@Entity
@Table(name = "cms_language")
@Getter
@Setter
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 语言（zh、en等）
     */
    @Column(name = "lang")
    private String lang;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

}
