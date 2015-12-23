/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 自定义模型
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_customModel")
@Getter
@Setter
public class CustomModel extends BaseEntity {

    private String name;//自定义模型名称
    private String description;//描述信息

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "custom")
    private List<CustomParam> params;

}
