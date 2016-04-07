/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huotu.huobanplus.common.model.RebatePair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 最基础的返利
 * Created by CJ on 11/6/15.
 */
@Data
public class Rebate {

    /**
     * Self
     * 按照Level,Ratio 分别储存
     */
    @JsonProperty("Self")
    private final List<RebatePair> selfRatios = new ArrayList<>();
    /**
     * BelongOne
     */
    @JsonProperty("BelongOne")
    private final List<RebatePair> belong1Ratios = new ArrayList<>();
    /**
     * BelongTwo,通常只有一个而且level必然是-1
     */
    @JsonProperty("BelongTwo")
    private final List<RebatePair> belong2Ratios = new ArrayList<>();
    /**
     * BelongThree,通常只有一个而且level必然是-1
     */
    @JsonProperty("BelongThree")
    private final List<RebatePair> belong3Ratios = new ArrayList<>();
    /**
     * LimitRatio
     */
    @JsonProperty("LimitRatio")
    private int limitRatio;
}
