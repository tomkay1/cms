package com.huotu.hotcms.admin.controller.decoration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     基础页装修控制器
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/basic")
public class BasicController {
    @RequestMapping("/head")
    public ModelAndView widgetTypeList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/basic/head.html");
        return  modelAndView;
    }
}
