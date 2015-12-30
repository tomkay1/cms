package com.huotu.hotcms.web.service;

import org.thymeleaf.engine.AttributeName;

/**
 * @brief Thymeleaf Html5 Attribute interface
 * @since 1.0.0
 * @author xhl
 * @time 2015/12/30
 */
public interface IDialectAttributeService {
    String getHtml5Attr(AttributeName attributeName,String name);

    Object getHtml5Attr(AttributeName attributeName);

}
