package com.huotu.hotcms.web.thymeleaf.expression;

import com.huotu.hotcms.web.service.SiteWebService;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.standard.expression.StandardExpressionObjectFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by cwb on 2015/12/31.
 */
public class HotExpressionObjectFactory extends StandardExpressionObjectFactory {


    public static final String SITESERVICE_EXPRESSION_OBJECT_NAME = "siteService";


    public HotExpressionObjectFactory() {
        super();
    }

    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    public Object buildObject(final IExpressionContext context, final String expressionObjectName) {


        if (SITESERVICE_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return new SiteWebService((IWebContext)context) {
            };
        }

        return super.buildObject(context, expressionObjectName);

    }
}
