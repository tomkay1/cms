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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

/**
 * 栏目节点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_category",uniqueConstraints = {@UniqueConstraint(columnNames = {"site,serial"})})
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 序列号
     */
    private String serial;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    private boolean deleted = false;

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
     * 栏目名称
     */
    @Column(name = "name")
    private String name;


    /**
     * 父级栏目
     */
    @ManyToOne
    @JoinColumn(name = "parentId")
    private Category parent;

    /**
     * 所有父级编号，用逗号分隔
     */
    @Column(name = "parentIds")
    private String parentIds;

    /**
     * 是否自定义模型
     */
    @Column(name = "custom")
    private boolean custom;

    /**
     * 系统数据类型ID/
     * 0:文章
     * 1：公告
     * 2：视频
     * 3：图库
     * 4：下载
     * 5：链接
     */
    @Column(name = "modelId")
    private Integer modelId;


    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;

    /**
     * 对应的requestUrl
     */
    @OneToOne
    @JoinColumn(name = "routeId")
    private Route route;

}
