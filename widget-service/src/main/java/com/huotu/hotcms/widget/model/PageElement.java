/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.model;


import com.huotu.hotcms.widget.Component;
import lombok.Data;

/**
 * 页面元素,可能为一个组件或者为一个布局
 *
 * @author CJ
 */
@Data
public class PageElement {

    private Component component;

    /**
     * 布局识别符号
     */
    private int[] layoutIndicates;

    /**
     * 页面元素,长度必须和{@link #layoutIndicates}一致
     */
    private PageElement[] elements;

}
