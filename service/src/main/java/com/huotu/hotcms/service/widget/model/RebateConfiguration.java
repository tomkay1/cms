/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import lombok.Data;

/**
 * 返利配置,是一个可储存的返利配置,应当与当前数据表设计适配.
 * 同时它可以生成一个可用的返利信息
 * Created by CJ on 11/6/15.
 */
@Data
public class RebateConfiguration {

    private RebateMode mode;
    /**
     * 保存实际乘数而非 乘以100以后的数据
     */
    private double saleRatio;
    private RebateSettings sale;
    /**
     * 保存实际乘数而非 乘以100以后的数据
     */
    private double quatoRadio;
    private RebateSettings quato;

}
