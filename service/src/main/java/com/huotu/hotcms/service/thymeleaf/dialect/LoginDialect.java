/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.dialect;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author CJ
 */
@Component
public class LoginDialect implements IExpressionObjectDialect, IDialect, IExpressionObjectFactory {

    private final Set<String> names;

    public LoginDialect() {
        super();
        Set<String> names = new HashSet<>();
        names.add("loginFailedMessage");
        this.names = Collections.unmodifiableSet(names);
    }

    private static String ResolveMessage(AuthenticationException exception, Locale locale) {
        // TODO 所有的AuthenticationException!!
        return "用户名或者密码不正确。";
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return this;
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return names;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if ("loginFailedMessage".equalsIgnoreCase(expressionObjectName)) {
            WebEngineContext context1 = (WebEngineContext) context;

            AuthenticationException authenticationException =
                    (AuthenticationException) context1.getRequest().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (authenticationException == null) {
                authenticationException = (AuthenticationException) context1.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }

            if (authenticationException == null)
                throw new IllegalStateException("No AuthenticationException Found");

            return LoginDialect.ResolveMessage(authenticationException, context.getLocale());
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return false;
    }
}
