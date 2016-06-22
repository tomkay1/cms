/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.widget.page.PageElement;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 组件
 *
 * @author CJ
 */
@Data
public class Component implements PageElement {

    private static final Log log = LogFactory.getLog(Component.class);

    private String widgetIdentity;

    private InstalledWidget widget;

    private String styleId;

    private ComponentProperties properties;

    private String previewHTML;

    public WidgetStyle currentStyle() {
        if (styleId == null) {
            log.debug("with null styleId, use default style instead.");
            return widget.getWidget().styles()[0];
        }


        for (WidgetStyle style : widget.getWidget().styles()) {
            if (styleId.equalsIgnoreCase(style.id()))
                return style;
        }
        log.debug("with " + styleId + " styleId, use default style instead because it's invalid.");
        return widget.getWidget().styles()[0];
    }

}
