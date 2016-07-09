/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.SiteManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 页面控制器
 * <p>
 * 还附带了一个功能{@link FilterBehavioral}可以进行选择过滤。</p>
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/page")
public class PageInfoController extends SiteManageController<PageInfo, Long, Void, Void> implements FilterBehavioral {

    private static final Log log = LogFactory.getLog(PageInfoController.class);

    private final Pattern pattern = Pattern.compile("^/([_a-zA-Z0-9]+)(/.*)?$");
    /**
     * 保护的path,这些path是系统使用的
     */
    private List<String> protectedPath = Arrays.asList("_web",
            "manage",
            "web",
            "bind",
            "interim",
            "shop",
            "admin"
    );

    @Override
    protected PageInfo preparePersist(Login login, Site site, PageInfo data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        if (data.getPagePath() != null && protectedPath.contains(data.getPagePath())) {
            throw new RedirectException("/manage/page", "这个路径无法使用。");
        }
        data.setCreateTime(LocalDateTime.now());
        data.setSite(site);
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/views/page/index.html";
    }

    @Override
    protected void prepareSave(Login login, PageInfo entity, PageInfo data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        throw new NoSuchMethodError("no support for save category");
    }

    @Override
    protected String openViewName() {
        return null;
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
