/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.test;

import com.huotu.cms.manage.service.SecurityService;
import com.huotu.hotcms.service.entity.login.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 帮助用户自动登录的Controller
 *
 * @author CJ
 */
@Controller
public class AuthController {

    private Login login;
    @Autowired
    private SecurityService securityService;

    public void setLogin(Login login) {
        this.login = login;
    }

    @RequestMapping("/testLoginAs")
    public void loginAs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityService.loginAs(request, response, login);
    }


}
