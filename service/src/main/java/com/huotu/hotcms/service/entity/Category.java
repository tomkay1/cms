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
import com.huotu.hotcms.service.Copyable;
import com.huotu.hotcms.service.common.ContentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 栏目节点（数据源）
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_category")
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class Category implements Auditable, Copyable<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 序列号
     */
    @Column(name = "serial", length = 100)
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
    @Column(name = "name", length = 60)
    private String name;

    /**
     * 父级栏目
     */
    @ManyToOne
    @JoinColumn(name = "parentId")
    private Category parent;


    /**
     * 正文类型
     * 如果已设置 {@link #parent}则不应该再设置该项.
     */
    @Column(name = "contentType")
    private ContentType contentType;

    /**
     * 所属站点
     */
    @ManyToOne
    @JoinColumn(name = "siteId")
    private Site site;

//    /**
//     * 所有父级编号，用逗号分隔
//     */
//    @Column(name = "parentIds")
//    private String parentIds;

//    /**
//     * 是否自定义模型
//     */
//    @Column(name = "custom")
//    private boolean custom;

//    /**
//     * 系统数据类型ID/
//     * 0:文章
//     * 1：公告
//     * 2：视频
//     * 3：图库
//     * 4：下载
//     * 5：链接
//     */
//    @Column(name = "modelId")
//    private Integer modelId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return orderWeight == category.orderWeight &&
                deleted == category.deleted &&
                Objects.equals(id, category.id) &&
                Objects.equals(serial, category.serial) &&
                Objects.equals(createTime, category.createTime) &&
                Objects.equals(updateTime, category.updateTime) &&
                Objects.equals(name, category.name) &&
                contentType == category.contentType &&
                Objects.equals(site, category.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial, orderWeight, deleted, createTime, updateTime, name, contentType, site);
    }

    @Override
    public Category copy() {
        Category category = new Category();
        category.setCreateTime(LocalDateTime.now());
        category.setContentType(contentType);
        category.setSerial(serial);
        category.setOrderWeight(orderWeight);
        category.setParent(parent);
        category.setSite(site);
        category.setUpdateTime(LocalDateTime.now());
        category.setDeleted(isDeleted());
        category.setName(name);
        return category;
    }

}
