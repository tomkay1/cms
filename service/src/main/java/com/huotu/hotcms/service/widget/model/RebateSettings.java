/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.huotu.huobanplus.common.model.Rebate;

/**
 * Created by CJ on 11/6/15.
 */
public class RebateSettings extends Rebate {

    public RebateSettings() {
    }

    public RebateSettings(Rebate rebate) {
        this.setLimitRatio(rebate.getLimitRatio());
        this.getSelfRatios().addAll(rebate.getSelfRatios());
        this.getBelong1Ratios().addAll(rebate.getBelong1Ratios());
        this.getBelong2Ratios().addAll(rebate.getBelong2Ratios());
        this.getBelong3Ratios().addAll(rebate.getBelong3Ratios());

    }
}
