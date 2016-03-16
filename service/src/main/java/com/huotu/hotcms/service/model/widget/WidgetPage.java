package com.huotu.hotcms.service.model.widget;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * <p>
 *   页面模型
 * </p>
 * @since 1.2
 *
 * @author xhl
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetPage {
    /**
    * 页面请求地址,不包含参数信息
    * **/
    @XmlAttribute(name = "url")
    private String url;

    /**
    * 页面中的布局列表
    * **/
    private WidgetLayout[] layout;
}
