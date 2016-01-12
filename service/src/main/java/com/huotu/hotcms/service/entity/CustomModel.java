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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 自定义模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_customModel")
@Getter
@Setter
public class CustomModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

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
     * 自定义模型名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述信息
     */
    @Column(name = "description")
    private String description;

    /**
     * 拥有的参数
     */
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "customModel")
    private List<CustomParam> params;

    public void addParam(CustomParam param) {
        param.setCustomModel(this);
        this.params.add(param);
    }
}
