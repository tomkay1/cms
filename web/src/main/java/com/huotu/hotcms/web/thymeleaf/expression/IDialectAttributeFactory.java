package com.huotu.hotcms.web.thymeleaf.expression;

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
    String getHtml5Attr(HttpServletRequest request, IProcessableElementTag elementTag,String name);

    Object getHtml5Attr(HttpServletRequest request,IProcessableElementTag elementTag);
}
