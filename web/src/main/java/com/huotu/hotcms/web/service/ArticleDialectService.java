package com.huotu.hotcms.web.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <P>
 *    文章输出
 * </P>
 * @since 1.0.0
 *
 * @author xhl
 *
 */
@Service
public class ArticleDialectService extends BaseDialectService{
    public ArticleDialectService() {
        super();
    }

    @Override
    public String resolveDataByAttr(IProcessableElementTag elementTag, AttributeName attributeName, String attributeValue) {
        return super.resolveDataByAttr(elementTag, attributeName, attributeValue);
    }
}

