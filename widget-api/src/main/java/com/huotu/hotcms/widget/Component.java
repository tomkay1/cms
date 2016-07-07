/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.fasterxml.jackson.annotation.*;
import com.huotu.hotcms.widget.page.AbstractPageElement;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 组件
 *
 * @author CJ
 */
@Data
@JsonTypeName("component")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Component extends AbstractPageElement {

    @JsonIgnore(value = true)
    private static final Log log = LogFactory.getLog(Component.class);

    private String widgetIdentity;

    //@JsonIgnore(value = true)//在生成的xml中忽略该属性，true即为忽略
    private InstalledWidget installedWidget;

    private String styleId;

    private ComponentProperties properties;

    @JsonIgnore(value = true)
    private String previewHTML;

    public WidgetStyle currentStyle() {
        if (styleId == null) {
            log.debug("with null styleId, use default style instead.");
            return installedWidget.getWidget().styles()[0];
        }


        for (WidgetStyle style : installedWidget.getWidget().styles()) {
            if (styleId.equalsIgnoreCase(style.id()))
                return style;
        }
        log.debug("with " + styleId + " styleId, use default style instead because it's invalid.");
        return installedWidget.getWidget().styles()[0];
    }

}
