package com.huotu.hotcms.web.service;

import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

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
   public Object  resolveDataByAttr(IProcessableElementTag elementTag,AttributeName attributeName) {
        return null;
    }
}
