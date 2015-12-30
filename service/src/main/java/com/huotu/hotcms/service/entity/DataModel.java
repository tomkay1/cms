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
import java.time.LocalDateTime;

/**
 * 数据模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_dataModel")
@Getter
@Setter
public class DataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 排序权重
     */
    @Column(name = "orderWeight")
    private int orderWeight;

    /**
     * 数据模型类型
     */
    @Column(name = "type")
    private ModelType type;

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
}
