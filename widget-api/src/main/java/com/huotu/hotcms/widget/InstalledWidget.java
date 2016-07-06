/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 已安装的控件,
 *
 * @author CJ
 */
@Data
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class InstalledWidget {

    private Long installWidgetId;

    @JsonIgnore(value = true)
    private Widget widget;
    // 其他属性 比如控件类型
    @JsonIgnore(value = true)
    private String type;

}
