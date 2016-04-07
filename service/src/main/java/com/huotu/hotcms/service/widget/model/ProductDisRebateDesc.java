/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huotu.huobanplus.common.model.adrebateconfig.RebateLayerConfig;
import lombok.Data;

import java.util.List;

/**
 * 某个货品设定的分校返利金额,含是否是个性化的返利设置
 * Created by allan on 2/1/16.
 */
@Data
public class ProductDisRebateDesc {
    @JsonProperty("proid")
    private int proId;
    private double amount;
    @JsonProperty("iscustom")
    private int isCustom;
    @JsonProperty("customcfg")
    private List<RebateLayerConfig> customConfig;
}
