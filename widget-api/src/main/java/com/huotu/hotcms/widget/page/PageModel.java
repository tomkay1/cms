/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.huotu.hotcms.widget.entity.PageInfo;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 系统和外部(比如我们的拖拽工具)页面的交互格式
 *
 * @author CJ
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PageModel implements Serializable {
    /**
     * 该页面的唯一ID 与PageInfo的pageID保持一致
     * 如果未null表示该页面尚未持久化
     *
     * @see PageInfo#pageId
     */
    @XmlAttribute(name = "pageIdentity")
    @JsonIgnore
    private Long pageIdentity;

    @XmlAttribute(name = "title")
    private String title;

    /**
     * 作为页面它只可拥有布局,不可以直接拥有组件。
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    private Layout[] elements;
}
