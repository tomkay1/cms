/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.ModelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 栏目节点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_category")
@Setter
@Getter
public class Category extends BaseEntity {

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
     * 系统数据类型
     */
    @Column(name = "modelType")
    private ModelType modelType;

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
    private RouteRule routeRule;

}
