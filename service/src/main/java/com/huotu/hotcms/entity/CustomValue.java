/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.entity;

import com.huotu.hotcms.common.ModelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 自定义模型参数值
 * Created by cwb on 2015/12/22.
 */
@Entity
@Table(name = "cms_customValue")
@Getter
@Setter
public class CustomValue extends DataEntity {

    private String value;//参数值

    @OneToOne
    private CustomParam customParam;

}
