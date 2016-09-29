/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity;

import com.huotu.hotcms.service.common.FieldType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 产品字段
 * 这个是属于CMS系统的产品字段,并不会因为商户删除某些产品或者产品目录而导致被删除
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_product_field"
        , indexes = {@Index(columnList = "name")}
        , uniqueConstraints = {@UniqueConstraint(columnNames = "serial")})
@Setter
@Getter
public class ProductField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 开发识别号;每一个字段都会提供一个特有的固定的开发识别号
     */
    @Column(length = 50)
    private String serial;
    /**
     * 字段名称
     */
    @Column(length = 20)
    private String name;

    /**
     * 这个字段可在一个产品中出现多次
     */
    private boolean repeatable;

    /**
     * 商城类目
     */
//    @OneToMany
//    private com.huotu.huobanplus.common.entity.Category mallCategory;

    // 数据类型
    private FieldType fieldType;


}
