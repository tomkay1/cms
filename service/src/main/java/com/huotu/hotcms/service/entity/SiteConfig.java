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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * <p>
 *     站点配置信息
 * </p>
 *
 * @since 1.2
 *
 * @author xhl
 */
@Entity
@Table(name = "cms_siteConfig")
@Setter
@Getter
public class SiteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 是否启用微官网功能
     */
    @Column(name = "enabledMobileSite")
    private boolean enabledMobileSite;

    /**
     * 微官网域名,目前只设置一个域名
     */
    @Column(name = "mobileDomain")
    private String mobileDomain;

    /**
     * 所属站点信息
     */
    @OneToOne
    @JoinColumn(name = "siteId")
    private Site site;
}
