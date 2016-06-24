/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.service;

import com.huotu.cms.manage.login.QuickAuthentication;
import com.huotu.hotcms.service.entity.login.Login;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CJ
 */
@Service
public class SecurityService {


    private SecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    /**
     * 获取指定环境的验证
     *
     * @param request
     * @param response
     * @return 获取
     */
    public Authentication currentAuthentication(HttpServletRequest request, HttpServletResponse response) {
        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        SecurityContext context = httpSessionSecurityContextRepository.loadContext(holder);
        return context.getAuthentication();
    }

    /**
     * 以指定身份作为安全系统认可的身份进行登录
     *
     * @param request
     * @param response
     * @param login
     * @return 参考返回值, 可作为Controller的结果, 也可以无视
     * @throws IOException
     */
    public String loginAs(HttpServletRequest request, HttpServletResponse response, Login login) throws IOException {
        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        SecurityContext context = httpSessionSecurityContextRepository.loadContext(holder);

        context.setAuthentication(new QuickAuthentication(login));

        httpSessionSecurityContextRepository.saveContext(context, holder.getRequest(), holder.getResponse());
//        SecurityContextHolder.setContext(context);
        response.sendRedirect("/manage/main");
        return null;
    }
}
