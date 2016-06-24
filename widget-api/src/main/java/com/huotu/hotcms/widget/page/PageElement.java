/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.page;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 页面元素,可能为一个组件或者为一个布局
 * <p>
 * 该接口尚未声明任何方式</p>
 *
 * @author CJ
 */
@JsonDeserialize(using = PageElementDeserializer.class)
public interface PageElement {

}
