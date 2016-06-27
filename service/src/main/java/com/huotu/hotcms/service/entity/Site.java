/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.login.Owner;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 站点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_site")
@Setter
@Getter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteId")
    private Long siteId;

    /**
     * 是否上架
     */
    private boolean enabled;

    /**
     * 所有主体
     */
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private Owner owner;

    /**
     * 站点名称
     */
    @Column(name = "name", length = 100)
    private String name;

    /**
     * 标题，填写有助于搜索引擎优化
     */
    @Column(name = "title", length = 200)
    private String title;

    /**
     * 关键字，填写有助于搜索引擎优化
     */
    @Column(name = "keywords")
    private String keywords;

    /**
     * 描述，填写有助于搜索引擎优化
     */
    @Column(name = "description")
    private String description;

    /**
     * 站点logo
     */
    @Column(name = "logoUri")
    private String logoUri;

    /**
     * 版权信息
     */
    @Column(name = "copyright")
    @Lob
    private String copyright;

    /**
     * 是否自定义模板
     */
    @Column(name = "custom")
    private boolean custom = false;

    /**
     * 自定义模板根路径
     */
    @Column(name = "customViewUrl")
    private String customTemplateUrl;

    /**
     * 站点是否个性化
     */
    @Column(name = "personalise")
    private boolean personalise;

    /**
     * 站点创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 站点更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 资源根路径(可以CDN方式读取缓存)
     */
    @Column(name = "resourceUrl")
    private String resourceUrl;

    /**
     * 网站类型(pc 商城or pc shop)
     */
    @Column(name = "siteType")
    private SiteType siteType;
    /**
     * 所属地区
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "regionId")
    private Region region;
//
//    public void addHost(Host host) {
//        if (this.hosts == null) {
//            this.hosts = new HashSet<>();
//        }
//        this.hosts.add(host);
//    }
//
//    public void stopHookSite(Host host) {
//        this.hosts.remove(host);
//    }
//
//
//    /**
//     * 什么域名可以被解析到这个站点来
//     */
//    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(name = "cms_site_host",
//            joinColumns = {@JoinColumn(name = "siteId", referencedColumnName = "siteId")},
//            inverseJoinColumns = {@JoinColumn(name = "hostId", referencedColumnName = "hostId")}
//    )
//    private Set<Host> hosts;
}
