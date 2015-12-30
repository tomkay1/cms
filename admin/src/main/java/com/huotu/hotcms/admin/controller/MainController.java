package com.huotu.hotcms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 视图控制器
 * @since  1.0.0
 * @author xhl
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping({"/index",""})
    public ModelAndView index(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/main.html");
        return modelAndView;
    }

    @RequestMapping("/columnList")
    public  ModelAndView columnList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/Section/columnList.html");
        return  modelAndView;
    }

    @RequestMapping("/articlelist")
    public ModelAndView articlelist(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/Contents/articlelist.html");
        return  modelAndView;
    }

    @RequestMapping("/announlist")
    public ModelAndView announlist(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/Contents/announlist.html");
        return  modelAndView;
    }

}
