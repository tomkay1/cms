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
 * 域名
 * Created by cwb on 2015/12/24.
 */
@Entity
@Table(name = "cms_host")
@Getter
@Setter
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 域名
     */
    @Column(name = "domain")
    private String domain;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;

    /**
     * 备注
     */
    private String remarks;
}
