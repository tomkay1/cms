package com.huotu.hotcms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2015/12/31.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/index")
    public ModelAndView Index(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/test.html");
        return  modelAndView;
    }
}
