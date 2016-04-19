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

import java.util.List;


/**
 * Created by allan on 2/1/16.
 */
@Data
public class RebateLayerConfig {
    /**
     * 索引号:0-&gt;自己;1-&gt;1级上线,以此类推
     */
    @JsonProperty("idx")
    private int index;
    /**
     * 统一设置的返利金额or百分比.-1时表示启用customVal
     */
    @JsonProperty("unified")
    private double unifiedVal;
    /**
     * 当前层级下的各个等级设置的返利百分比or金额
     */
    @JsonProperty("custom")
    private List<RebateLevelConfig> customVals;
}
