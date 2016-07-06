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

/**
 * 页面控制器
 * <p>
 * 还附带了一个功能{@link FilterBehavioral}可以进行选择过滤。
 *
 * @author CJ
 */
@Controller
@RequestMapping("/manage/page")
public class PageInfoController extends SiteManageController<PageInfo, Long, Void, Void> implements FilterBehavioral {

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
        String uri = request.getRequestURI();
        String contextUri = uri.substring(request.getContextPath().length());
        int first = contextUri.indexOf('/');
        if (first == -1) {
            if (protectedPath.contains(contextUri)) {
                return FilterStatus.CHAIN;
            } else
                return FilterStatus.NEXT;
        } else {
            String firstUri = contextUri.substring(0, first - 1);
            if (protectedPath.contains(contextUri)) {
                return FilterStatus.CHAIN;
            } else
                return FilterStatus.NEXT;
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
