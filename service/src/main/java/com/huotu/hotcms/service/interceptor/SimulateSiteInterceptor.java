/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CJ
 */
@Component
public class SimulateSiteInterceptor extends HandlerInterceptorAdapter implements WebRequestInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler
            , ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        if (request.getParameter("simulateSite") != null && modelAndView != null) {
            modelAndView.addObject("simulateSite", request.getParameter("simulateSite"));
        }
    }

    @Override
    public void preHandle(WebRequest request) throws Exception {

    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
//if (request.getParameter("simulateSite"))
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}
