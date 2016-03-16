package com.huotu.hotcms.service.model.widget;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


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

    /**
    * 页面布局模块
    * */
    private WidgetModule[] module;
}
