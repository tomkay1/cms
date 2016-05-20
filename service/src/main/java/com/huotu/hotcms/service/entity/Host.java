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
import java.util.*;

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
    @Column(name = "hostId")
    private Long hostId;
    
    /**
     * 序列号
     */
    private String serial;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 域名
     */
    @Column(name = "domain",unique = true)
    private String domain;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 主推域名,一般一个网站只有一个主推域名
     * */
    @Column(name = "home")
    private Boolean home;

    /**
     * 对应站点
     */
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE,CascadeType.REFRESH},mappedBy = "hosts")
    private Set<Site> sites;

    public void addSite(Site site) {
        site.addHost(this);
    }

    public void removeSite(Site site) {
        site.removeHost(this);
    }
}
