package com.huotu.cms.manage.controller;

import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.hotcms.service.service.MallService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.huobanplus.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity mallLogin(String username, String password, HttpServletResponse response) {
        try {
            User user = mallService.mallLogin(CMSContext.RequestContext().getSite().getOwner(), username, password, response);
            if (user != null) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body("{status:true,message:'登录成功!'}");
            }
        } catch (IOException | LoginException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body("{status:false,message='" + e.getMessage() + "'}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON_UTF8)
                .body("{status:false,message:'登录失败！'}");
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String mallRegister(String userPhone, String password, HttpServletResponse response) {
        try {
            mallService.mallRegister(CMSContext.RequestContext().getSite().getOwner(), userPhone, password, response);
        } catch (IOException | RegisterException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
