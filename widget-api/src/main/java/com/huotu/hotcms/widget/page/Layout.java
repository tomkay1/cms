/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.huotu.hotcms.widget.Component;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
     *
     * @see #columns()
     */
    private String value;

    private StyleSheet styleSheet;
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
     * 来自web的数据,可能会有一些对齐的问题
     *
     * @param layout 编辑器提供的
     * @return 有效的Layout
     */
    static Layout FromWeb(Layout layout) {
        Layout value = new Layout();
        value.value = layout.value;
        value.elementGroups = new PageElement[value.columns().length][];
        value.styleSheet = layout.styleSheet;
        for (int i = 0; i < value.elementGroups.length; i++) {
            //检测输入方是否输入了足够的数据
            PageElement[] newGroup;
            if (layout.elementGroups.length > i) {
                newGroup = new PageElement[layout.elementGroups[i].length];
                for (int j = 0; j < newGroup.length; j++) {
                    PageElement newPageElement;
                    PageElement src = layout.elementGroups[i][j];
                    if (src instanceof Layout) {
                        newPageElement = FromWeb((Layout) src);
                    } else
                        newPageElement = src;
                    newGroup[j] = newPageElement;
                }
            } else {
                newGroup = new PageElement[]{new Empty()};
            }
            value.elementGroups[i] = newGroup;
        }

        return value;
    }

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

    /**
     * @return 所有组件
     */
    public Collection<Component> components() {
        ArrayList<Component> components = new ArrayList<>();
        for (PageElement element : elements()) {
            if (element instanceof Layout) {
                components.addAll(((Layout) element).components());
            } else if (element instanceof Component) {
                components.add((Component) element);
            }
        }
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layout)) return false;
        Layout layout = (Layout) o;
        return Objects.equals(value, layout.value) &&
                Arrays.deepEquals(elementGroups, layout.elementGroups) &&
                Objects.equals(styleSheet, layout.styleSheet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, elementGroups, styleSheet);
    }

    /**
     * @return 每一个column的占位数字
     */
    public String[] columns() {
        return value.split(",");
    }

    @Override
    public void printAttributesAsHtml(Writer writer) throws IOException {
        if (styleSheet != null)
            styleSheet.printHtml(writer);
    }
}
