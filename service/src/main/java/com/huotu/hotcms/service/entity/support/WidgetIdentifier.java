/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.support;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 控件唯一识别符
 *
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WidgetIdentifier implements Serializable {

    private static final long serialVersionUID = -5647085640723572029L;
    private String groupId;
    private String widgetId;
    private String version;

    /**
     * @param identify groupId-widgetId:version
     * @return
     * @throws IllegalArgumentException identify不符合预定规则
     */
    public static WidgetIdentifier valueOf(String identify) throws IllegalArgumentException {
        try {
            String[] args = identify.split(":");
            String[] groupIdAndWidgetId = args[0].split("-");
            return new WidgetIdentifier(groupIdAndWidgetId[0], groupIdAndWidgetId[1], args[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("参数不符合预定规则");
        }
    }

    @Override
    public String toString() {
        return groupId + "-" + widgetId + ":" + version;
    }
}
