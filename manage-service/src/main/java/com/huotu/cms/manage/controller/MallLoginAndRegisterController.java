package com.huotu.cms.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lhx on 2016/11/25.
 */
@Controller
@RequestMapping("/mall/")
public class MallLoginAndRegisterController {

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String mallLogin() {
        return null;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String mallRegister() {
        return null;
    }
}
