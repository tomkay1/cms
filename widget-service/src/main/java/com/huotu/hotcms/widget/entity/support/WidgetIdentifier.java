/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.entity.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 控件唯一识别符
 *
 * @author CJ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetIdentifier implements Serializable {

    private static final long serialVersionUID = -5647085640723572029L;
    private String groupId;
    private String widgetId;
    private String version;

}
