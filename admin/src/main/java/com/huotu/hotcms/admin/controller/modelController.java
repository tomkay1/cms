package com.huotu.hotcms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/model")
public class modelController {

    @RequestMapping("/modellist")
    public ModelAndView modelList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/modellist.html");
        return  modelAndView;
    }

    @RequestMapping("/addModel")
    public ModelAndView addModel(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/addModel.html");
        return modelAndView;
    }
}
