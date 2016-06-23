/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.interceptor;

import com.huotu.hotcms.service.common.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于管理后台的拦截器,它作用于所有请求
 */
@Component
public class ManageInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private CookieUser cookieUser;

    @Autowired
    private ConfigInfo configInfo;
    private SecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
            SecurityContext context = httpSessionSecurityContextRepository.loadContext(holder);
            if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
                modelAndView.addObject("mallManageUrl", configInfo.getMallManageUrl());
            }

            response.setHeader("X-Frame-Options", "Allow");
        }
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
