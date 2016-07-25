/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author CJ
 */
@Primary
@Component(RequestContextUtils.REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME)
public class CMSRequestDataValueProcessor implements RequestDataValueProcessor {

    private Pattern DISABLE_CSRF_TOKEN_PATTERN = Pattern
            .compile("(?i)^(GET|HEAD|TRACE|OPTIONS)$");

    private String DISABLE_CSRF_TOKEN_ATTR = "DISABLE_CSRF_TOKEN_ATTR";

    public String processAction(HttpServletRequest request, String action) {
        return action;
    }

    public String processAction(HttpServletRequest request, String action, String method) {
        if (method != null && DISABLE_CSRF_TOKEN_PATTERN.matcher(method).matches()) {
            request.setAttribute(DISABLE_CSRF_TOKEN_ATTR, Boolean.TRUE);
        } else {
            request.removeAttribute(DISABLE_CSRF_TOKEN_ATTR);
        }
        return action;
    }

    public String processFormFieldValue(HttpServletRequest request, String name,
                                        String value, String type) {
        return value;
    }

    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        if (Boolean.TRUE.equals(request.getAttribute(DISABLE_CSRF_TOKEN_ATTR))) {
            request.removeAttribute(DISABLE_CSRF_TOKEN_ATTR);
            return Collections.emptyMap();
        }

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return Collections.emptyMap();
        }
        Map<String, String> hiddenFields = new HashMap<String, String>(1);
        hiddenFields.put(token.getParameterName(), token.getToken());
        return hiddenFields;
    }

    public String processUrl(HttpServletRequest request, String url) {
        if (url == null)
            return null;
        if (request.getParameter("simulateSite") != null) {
            if (url.contains("simulateSite"))
                return url;
            if (url.contains("?"))
                return url + "&simulateSite=" + request.getParameter("simulateSite");
            return url + "?simulateSite=" + request.getParameter("simulateSite");
        }
        return url;
    }
}
