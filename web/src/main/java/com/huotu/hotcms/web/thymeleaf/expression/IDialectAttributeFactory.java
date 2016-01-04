package com.huotu.hotcms.web.thymeleaf.expression;

import org.thymeleaf.model.IProcessableElementTag;

/**
 * <p>
 *     Thymeleaf Html5 Attribute interface
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public interface IDialectAttributeFactory {
    String getHtml5Attr(IProcessableElementTag elementTag,String name);

    Object getHtml5Attr(IProcessableElementTag elementTag);
}
