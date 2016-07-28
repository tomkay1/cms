/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 页面布局以及组件
 *
 * @author CJ
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PageLayout implements Serializable {

    private static final long serialVersionUID = 5975200980037195706L;

    private static final Layout[] VoidLayouts = new Layout[0];
    /**
     * 作为页面它只可拥有布局,不可以直接拥有组件。
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    private Layout[] root;

    /**
     * @param pageLayout 页面布局
     * @return 一个不可能为null的布局数组
     */
    public static Layout[] NoNullLayout(PageLayout pageLayout) {
        if (pageLayout == null)
            return VoidLayouts;
        if (pageLayout.root == null)
            return VoidLayouts;
        return pageLayout.root;
    }

    /**
     * 来自web的数据,可能会有一些对齐的问题
     *
     * @param root 编辑器提供的
     * @return 有效的PageLayout
     */
    public static PageLayout FromWeb(Layout[] root) {
        PageLayout layout = new PageLayout();
        layout.root = new Layout[root.length];
        for (int i = 0; i < root.length; i++) {
            layout.root[i] = Layout.FromWeb(root[i]);
        }
        return layout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageLayout)) return false;
        PageLayout that = (PageLayout) o;
        return Arrays.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(root);
    }
}
