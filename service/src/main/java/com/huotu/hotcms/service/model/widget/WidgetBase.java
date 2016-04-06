package com.huotu.hotcms.service.model.widget;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;


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
@XmlSeeAlso({WidgetBase.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetBase {

    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "widgetUri")
    private String widgetUri;

    @XmlAttribute(name = "widgetEditUri")
    private String widgetEditUri;

    /**
     * 所属布局ID
     * */
    @XmlAttribute(name = "layoutId")
    private String layoutId;

    /**
     * 所属布局位置模块index
     * **/
    @XmlAttribute(name = "layoutPosition")
    private String layoutPosition;

//    private List<WidgetProperty> property;
//    @XmlAnyElement
//    @XmlElementWrapper(name="property")
//    private Map property;

//    @XmlElementWrapper(name="widgetProperty")
//    private WidgetListProperty property;

    @XmlElement(name = "property")
    private List<WidgetProperty> property;

    private Boolean edit;
}
