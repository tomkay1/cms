/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.service;

import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 最终前端-页面 过滤行为
 *
 * @author CJ
 */
@Service
public class PageFilterBehavioral implements FilterBehavioral {

    /**
     * 保护的path,这些path是系统使用的
     */
    public static final List<String> protectedPath = Collections.unmodifiableList(Arrays.asList("_web",
            "auth",
            "login",
            "logout",
            "manage",
            "web",
            "bind",
            "interim",
            "shop",
            "admin",
            "dataSource",
            "preview",
            "previewHtml",
            "widget",
            //这是一个很特殊的 测试的模拟登陆 :)
            "testLoginAs"
    ));
    private static final Log log = LogFactory.getLog(PageFilterBehavioral.class);
    private final Pattern pattern = Pattern.compile("^/([_a-zA-Z0-9]+)(/.*)?$");

    /**
     * @param pagePath path
     * @return 是否允许用户使用指定的pagePath
     */
    public boolean ableToUse(String pagePath) {
        if (protectedPath.contains(pagePath))
            return false;
        for (String path : protectedPath) {
            try {
                Pattern pattern = Pattern.compile("^" + path + "(/|/.*)$");
                Matcher matcher = pattern.matcher(pagePath);
                if (matcher.matches())
                    return false;
            } catch (PatternSyntaxException ignored) {
                // 没事
            }
        }
        return true;
    }

    @Override
    public FilterStatus doSiteFilter(Site site, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String uri = request.getServletPath();

        String contextUri;
        if (uri.length() > 0)
            contextUri = uri;
        else {
            contextUri = request.getRequestURI().substring(request.getContextPath().length());
        }

        try {
            String firstPath = firstPath(contextUri);

            if (protectedPath.contains(firstPath)) {
                return FilterStatus.CHAIN;
            } else
                return FilterStatus.NEXT;
        } catch (IllegalStateException ex) {
            log.debug("doSiteFilter", ex);
            return FilterStatus.CHAIN;
        }

    }

    /**
     * 从uri中获取第一个path
     * 比如uri=/foo/bar  应该获取foo
     * uri=/foo   应该获取foo
     * uri=       应该获取
     * <a href="https://regexper.com/#%5E%5C%2F(%5B_a-zA-Z0-9%5D%2B)(%5C%2F.*)%3F%24">正则</a>
     *
     * @param uri
     * @return path
     */
    private String firstPath(String uri) {
        if (uri.length() == 0)
            return uri;
        if (uri.equals("/"))
            return "";
        Matcher matcher = pattern.matcher(uri);
        if (!matcher.matches())
            throw new IllegalStateException("Bad Content URI:" + uri);
        return matcher.group(1);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
