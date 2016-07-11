/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Enabled;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 站点路由规则
 * 路由应当是简单的从请求规则1 转发至结果URL 并且是内部转发
 * Created by xhl on 2015/12/21.
 */
@Entity
@Table(name = "cms_route")
@Setter
@Getter
public class Route implements Auditable, Enabled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;
    /**
     * 路由规则,标准正则表达式
     */
    @Column(name = "rule", length = 50)
    private String rule;

    /**
     * 包含 $1 $2 的替换uri
     */
    @Column(length = 50)
    private String targetUri;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;

    /**
     * 路由描述信息
     */
    @Column(name = "description")
    private String description;
}
