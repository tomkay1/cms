package com.huotu.hotcms.service.model.widget;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;


/**
 * <p>
 *   控件主体模型(比如商品列表组件、客服组件等)
 * </p>
 * @since 1.2
 *
 * @author xhl
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetBase {

    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "widgetUri")
    private String widgetUri;

//    private WidgetProperty[] property;

    private List<WidgetProperty> property;
}
