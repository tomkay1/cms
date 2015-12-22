package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.admin.web.CookieUser;
import com.huotu.hotcms.admin.web.QueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器.
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/login.jsp", "/Login/","backui/","frontui/"};

    @Autowired
    private  CookieUser cookieUser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer customerId= QueryHelper.getQueryValInteger(request,"customerid");
        if(!cookieUser.checkLogin(request,customerId))
        {
            response.sendRedirect("http://login.pdmall.com");//后面换成都配置文件方式来读取跳转登录页面
        }
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
