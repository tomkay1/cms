package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.admin.web.CookieUser;
import com.huotu.hotcms.admin.web.QueryHelper;
import com.huotu.hotcms.common.ConfigInfo;
import com.huotu.hotcms.entity.others.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器.
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
//    private static final String[] IGNORE_URI = {"/login.jsp", "/Login/","backui/","frontui/"};

    @Autowired
    private  CookieUser cookieUser;

    @Autowired
    private ConfigInfo configInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer customerId= QueryHelper.getQueryValInteger(request,"customerid");
        if(!cookieUser.checkLogin(request,customerId))
        {
            String loginUrl=configInfo.getOutLoginUrl();
            response.sendRedirect(loginUrl);
            return  false;
        }
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null) {//加载用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setCustomerId(QueryHelper.getQueryValInteger(request, "customerid"));
            userInfo.setIsSuperManage(cookieUser.getRoleId(request) == -1);
            modelAndView.addObject("user",userInfo);
        }
    }
}
