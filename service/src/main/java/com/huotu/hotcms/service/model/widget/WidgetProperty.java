package com.huotu.hotcms.service.model.widget;

import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 页面中控件主体设置属性模型
 * Created by Administrator xhl 2016/3/15
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetProperty {
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "value")
    private String value;
}
