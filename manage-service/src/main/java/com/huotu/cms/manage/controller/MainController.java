/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.login.Manager;
import com.huotu.cms.manage.login.QuickAuthentication;
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 视图控制器
 *
 * @author xhl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/manage/main")
public class MainController {

    private static final Log log = LogFactory.getLog(MainController.class);
    @Autowired
    private ConfigInfo configInfo;
    @Autowired
    private CookieUser cookieUser;
    @Autowired
    private OwnerRepository ownerRepository;
    private SecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();


    private String loginAs(HttpServletRequest request, HttpServletResponse response, Login login) throws IOException {
        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        SecurityContext context = httpSessionSecurityContextRepository.loadContext(holder);

        context.setAuthentication(new QuickAuthentication(login));

        httpSessionSecurityContextRepository.saveContext(context, holder.getRequest(), holder.getResponse());
        response.sendRedirect("/manage/main");
        return null;
    }

    @RequestMapping("/login")
    public String loginPage(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        if (cookieUser.isSupper(request)) {
            log.debug("LoginAs Manager");
            //以管理员身份登录
            return loginAs(request, response, new Manager());
        }

        int customerId = cookieUser.getCustomerId(request);
        if (customerId > 0) {
            log.debug("LoginAS Mall User:" + customerId);
            Owner owner = ownerRepository.findByCustomerId(customerId);
            if (owner != null) {
                log.debug("LoginAS Owner:" + owner);
                return loginAs(request, response, owner);
            }
        }

        model.addAttribute("mallLoginUrl", configInfo.getOutLoginUrl());
        // 检查cookie
        return "login.html";
    }

    @RequestMapping({"/index", ""})
    public ModelAndView index(@AuthenticationPrincipal Login login) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/main.html");
        return modelAndView;
    }

    @RequestMapping(value = "/decorated")
    public ModelAndView decorated(String scope) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scope", scope);
        modelAndView.setViewName("/decoration/decorated.html");
        return modelAndView;
    }
}
