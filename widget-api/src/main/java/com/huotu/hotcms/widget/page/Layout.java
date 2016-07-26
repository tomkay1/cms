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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * 页面布局
 *
 * @author CJ
 */
@ToString
@Setter
@Getter
@JsonTypeName("layout")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Layout implements PageElement {

    private static final long serialVersionUID = -5706839275588228234L;
    /**
     * 用逗号间隔的bootstrap栅格参数，总值必须为12
     * <p>比如</p>
     * <ul>
     * <li>12</li>
     * <li>1,11</li>
     * <li>4,6,2</li>
     * </ul>
     */
    private String value;
//
//    /**
//     * valued的split值决定有几个elements
//     */
//    @JacksonXmlElementWrapper(useWrapping=false)
//    private PageElement[] elements;

    /**
     * 分组好的element,它的length必须等同 valued的split值
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    private PageElement[][] elementGroups;

    /**
     * 所谓平行元素就是每一个group里只有一个元素的结构
     *
     * @param elements 平行元素
     */
    public void setParallelElements(PageElement[] elements) {
        elementGroups = new PageElement[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            elementGroups[i] = new PageElement[]{elements[i]};
        }
    }

    /**
     * @return 直接返回所有PageElement
     */
    public Iterable<PageElement> elements() {
        ArrayList<PageElement> list = new ArrayList<>();

        Arrays.stream(elementGroups)
                .forEach(elements -> Arrays.stream(elements).forEach(list::add));

        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layout)) return false;
        Layout layout = (Layout) o;
        return Objects.equals(value, layout.value) &&
                Arrays.deepEquals(elementGroups, layout.elementGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, elementGroups);
    }
}
