package com.huotu.hotcms.web.thymeleaf.expression;

import org.thymeleaf.engine.AttributeName;

/**
 * @brief Thymeleaf Html5 Attribute interface
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
public interface IDialectAttributeFactory {
    String getHtml5Attr(AttributeName attributeName,String name);

    Object getHtml5Attr(AttributeName attributeName);
}
