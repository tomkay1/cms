/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.huotu.hotcms.widget.page.PageElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Objects;


/**
 * 组件
 *
 * @author CJ
 */
@Setter
@Getter
@ToString
@JsonTypeName("component")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Component implements PageElement {


    @JsonIgnore
    private static final Log log = LogFactory.getLog(Component.class);
    private static final long serialVersionUID = 2465181654018260345L;

    /**
     * 语义等同{@link WidgetLocateService#findWidget(String)}中的identify
     */
    private String widgetIdentity;

    /**
     * 如果界面上存在同样的组件，要拿ID去标识
     */
    private String id;

    @JsonIgnore//在生成的xml中忽略该属性，true即为忽略
    private InstalledWidget installedWidget;


    private String styleId;

    private ComponentProperties properties;

    //@JsonIgnore(value = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Component)) return false;
        Component component = (Component) o;
        return Objects.equals(widgetIdentity, component.widgetIdentity) &&
                Objects.equals(id, component.id) &&
                Objects.equals(styleId, component.styleId) &&
                Objects.equals(properties, component.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetIdentity, id, styleId, properties);
    }
}
