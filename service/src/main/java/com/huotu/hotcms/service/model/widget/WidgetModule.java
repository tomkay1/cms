package com.huotu.hotcms.service.model.widget;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * <p>
 *   布局模块模型(比如左、中、右模块)
 * </p>
 * @since 1.2
 *
 * @author xhl
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetModule {
    /**
     * 组件模块的定位,根据布局来（layout）
     * */
    @XmlAttribute(name = "position")
    private String position;

    private WidgetBase[] widget;
}
