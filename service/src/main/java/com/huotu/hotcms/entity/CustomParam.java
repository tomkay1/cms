/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import com.huotu.hotcms.common.ParamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 模型参数
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_customParam")
@Getter
@Setter
public class CustomParam extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customModelId")
    private CustomModel customModel;
    private String name;//参数名称
    private ParamType type;//参数类型
    @OneToOne
    private Custom custom;
}
