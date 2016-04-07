/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 等级对应的返利百分比or金额
 * Created by allan on 2/1/16.
 */
@Data
public class RebateLevelConfig {
    /**
     * 等级id
     */
    @JsonProperty("lvid")
    private int levelId;
    /**
     * 返利金额or百分比
     */
    @JsonProperty("val")
    private double value;
}
