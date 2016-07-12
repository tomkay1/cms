/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 已安装的控件,
 *
 * @author CJ
 */
@Setter
@Getter
@ToString
public class InstalledWidget {

    private final transient Widget widget;

    /**
     * 所属的控件包的识别符,如果该控件不属于任何控件包则未null
     */
    private WidgetIdentifier identifier;
    private Long ownerId;
    private String type;

    public InstalledWidget(Widget widget) {
        this.widget = widget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstalledWidget)) return false;
        InstalledWidget that = (InstalledWidget) o;
        return Objects.equals(widget.groupId(), that.widget.groupId()) &&
                Objects.equals(widget.widgetId(), that.widget.widgetId()) &&
                Objects.equals(widget.version(), that.widget.version()) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(widget.groupId(), widget.widgetId(), widget.version(), identifier, ownerId, type);
    }
}
