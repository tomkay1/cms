package com.huotu.hotcms.web.service;

import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

/**
 * <P>
 *    Thymeleaf 自定义方言循环数据解析基类
 * </P>
 * @since 1.0.0
 *
 * @author xhl
 *
 */
public abstract class BaseDialectService {
   public Object  resolveDataByAttr(HttpServletRequest request,IProcessableElementTag elementTag,AttributeName attributeName) {
        return null;
    }

    public String resolveDataByAttr(HttpServletRequest request,IProcessableElementTag elementTag,AttributeName attributeName,String attributeValue)
    {
        return  null;
    }
}
