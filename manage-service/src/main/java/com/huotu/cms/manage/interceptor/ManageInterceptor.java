/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.interceptor;

import com.huotu.cms.manage.service.SecurityService;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.ConfigInfo;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.widget.CMSContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 用于管理后台的拦截器,它作用于所有请求
 */
@Component
public class ManageInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private CookieUser cookieUser;

    @Autowired
    private ConfigInfo configInfo;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private SiteService siteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = securityService.currentAuthentication(request, response);
        if (authentication != null && authentication.getPrincipal() != null) {
            Login login = (Login) authentication.getPrincipal();
            if (login.currentSiteId() != null) {
                Site site = siteService.getSite(login.currentSiteId());
                CMSContext.RequestContext().setSite(site);
                CMSContext.RequestContext().setLocale(site.getRegion() == null ? request.getLocale()
                        : site.getRegion().getLocale());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Authentication authentication = securityService.currentAuthentication(request, response);
            if (authentication != null && authentication.isAuthenticated()) {
                modelAndView.addObject("mallManageUrl", configInfo.getMallManageUrl());
                Login login = (Login) authentication.getPrincipal();

                // 推荐站点
                Site sitePreferences = null;
                if (login.currentOwnerId() != null) {
                    Set<Site> siteSet = siteService.findByOwnerIdAndDeleted(login.currentOwnerId(), false);
                    modelAndView.addObject("siteSet", siteSet);
                    if (siteSet.size() == 1) {
                        sitePreferences = siteSet.stream().findAny().orElse(null);
                    }
                }

                // 干嘛自动为用户设置?
                if (login.currentSiteId() != null) {
                    Site manageSite = siteService.getSite(login.currentSiteId());
                    if (login.siteManageable(manageSite)) {
                        modelAndView.addObject("manageSite", manageSite);
                    } else {
                        login.updateSiteId(null);
                    }
                } else if (sitePreferences != null) {
                    // 如果当前用户只有一个site
                    modelAndView.addObject("manageSite", sitePreferences);
                }

            }
        }
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
//        String servletPath = request.getServletPath();
//        if (modelAndView != null) {//加载用户信息
//            UserInfo userInfo = new UserInfo();
//            userInfo.setCustomerId(QueryHelper.getQueryValInteger(request, "customerid"));
//            userInfo.setOwnerId(QueryHelper.getQueryValInteger(request, "customerid"));
//            userInfo.setIsSuperManage(cookieUser.getRoleId(request) == -1);
//            if (cookieUser.isSupper(request)) {
//                modelAndView.addObject("mallManageUrl", configInfo.getMallSupperUrl(userInfo.getCustomerId()));
//            } else {
//                modelAndView.addObject("mallManageUrl", configInfo.getMallManageUrl());
//            }
//            modelAndView.addObject("user", userInfo);
//        }
    }
}
