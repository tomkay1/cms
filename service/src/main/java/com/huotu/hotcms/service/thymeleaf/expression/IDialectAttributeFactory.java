/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.expression;

import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     Thymeleaf Html5 Attribute interface
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public interface IDialectAttributeFactory {
    String getHtml5Attr(HttpServletRequest request, IProcessableElementTag elementTag, String name);

    Object getHtml5Attr(HttpServletRequest request, IProcessableElementTag elementTag);
}
