package com.huotu.hotcms.service.model.widget;

import com.huotu.hotcms.service.common.LayoutEnum;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;


/**
 * <p>
 *   布局模型(比如左、中、右布局、左右布局等)
 * </p>
 * @since 1.2
 *
 * @author xhl
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetLayout {
    @XmlAttribute(name = "layoutId")
    private String layoutId;

    @XmlAttribute(name = "layoutType")
    private int layoutType;

    /**
    * 页面布局模块
    * */
    private List<WidgetModule> module;
}
