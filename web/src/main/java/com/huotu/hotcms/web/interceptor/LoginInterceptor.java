package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.web.util.web.CookieUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器.
 * @since 1.0.0
 * @author xhl
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/", "/f/**","/f"};

    @Autowired
    private CookieUser cookieUser;

    @Autowired
    private ConfigInfo configInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.print("!212");
//        if(Arrays.asList(IGNORE_URI).contains(request.getServletPath())) {
//            return true;
//        }
//        Integer customerId= QueryHelper.getQueryValInteger(request, "customerid");
//
//        if(!cookieUser.checkLogin(request,response,customerId))//判断用户登录授权
//        {
//            String loginUrl=configInfo.getOutLoginUrl();
//            response.sendRedirect(loginUrl);
//            return  false;
//        }
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.print("!212");
//        String servletPath=request.getServletPath();
//        if(modelAndView!=null) {//加载用户信息
//            UserInfo userInfo = new UserInfo();
//            userInfo.setCustomerId(QueryHelper.getQueryValInteger(request, "customerid"));
//           111 userInfo.setIsSuperManage(cookieUser.getRoleId(request) == -1);
//            if(cookieUser.isSupper(request)){
//                modelAndView.addObject("mallManageUrl",configInfo.getMallSupperUrl(userInfo.getCustomerId()));
//            }else{
//                modelAndView.addObject("mallManageUrl",configInfo.getMallManageUrl());
//            }
//            modelAndView.addObject("user",userInfo);
//        }
    }
}
