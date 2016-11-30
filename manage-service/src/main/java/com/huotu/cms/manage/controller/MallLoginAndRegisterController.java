package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.hotcms.service.service.MallService;
import com.huotu.hotcms.widget.CMSContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lhx on 2016/11/25.
 */
@Controller
@RequestMapping("/mall/")
public class MallLoginAndRegisterController {

    @Autowired
    MallService mallService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String mallLogin(String username, String password, HttpServletResponse response) {
        try {
            mallService.mallLogin(CMSContext.RequestContext().getSite().getOwner(), username, password, response);
        } catch (IOException | LoginException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String mallRegister(String username, String password, HttpServletResponse response) {
        try {
            mallService.mallRegister(CMSContext.RequestContext().getSite().getOwner(), username, password, response);
        } catch (IOException | RegisterException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
