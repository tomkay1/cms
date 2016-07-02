/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;


/**
 * 页面布局
 *
 * @author CJ
 */
@Data
@JsonTypeName("layout")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Layout implements PageElement {

    /**
     * Layout的布局参数：比如12,6-6,4-4-4
     */
    private String value;

    @JacksonXmlElementWrapper(useWrapping=false)
    private PageElement[] elements;
}
