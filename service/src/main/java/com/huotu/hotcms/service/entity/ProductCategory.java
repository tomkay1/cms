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

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 产品数据源,跟其他数据源的差别就是产品还拥有产品信息,比如是一个伙伴商城商品还是自定义商品,以及一些自定义字段
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_product_category")
@Setter
@Getter
public class ProductCategory extends Category {

    /**
     * 使用伙伴商城的产品信息
     */
    private boolean huobanMall;

    @OneToMany
    private List<ProductField> fieldList;
}
