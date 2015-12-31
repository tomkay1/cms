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

/**
 * 自定义模型值记录表
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_custom")
@Getter
@Setter
public class Custom extends BaseEntity {

    /**
     * 参数值
     */
    @Column(name = "value")
    private String value;


    @OneToOne
    @JoinColumn(name = "customParam")
    private CustomParam customParam;

    /**
     * 所属栏目
     */
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}