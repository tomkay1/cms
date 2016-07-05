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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @deprecated 准备删除, 无需该逻辑
 */
@Entity
@Table(name = "cms_customPages")
@Getter
@Setter
@Deprecated
public class CustomPages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 序列号  暂定生成规则是：site_{siteID}_serial
     */
    private String serial;

    /**
     * 页面名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 页面描述信息
     * */
    @Column(name = "description")
    private String description;

    /**
     * 是否为首页
     * */
    @Column(name = "home")
    private boolean home=false;

    /**
    * 是否发布
    * */
    @Column(name ="publish")
    private boolean publish=false;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;
}
