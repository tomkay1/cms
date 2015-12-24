package com.huotu.hotcms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/12/23.
 */
@Controller
@RequestMapping("/main")
public class mainController {
    @RequestMapping({"/index","/"})
    public ModelAndView index(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/View/main.html");
        return modelAndView;
    }

    @RequestMapping("/list")
    public  ModelAndView list(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/section/list.html");
        return  modelAndView;
    }

    @RequestMapping("/modellist")
    public ModelAndView modelList(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/modellist.html");
        return  modelAndView;
    }

    @RequestMapping("/articlelist")
    public ModelAndView articlelist(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/Contents/articlelist.html");
        return  modelAndView;
    }

    @RequestMapping("/announlist")
    public ModelAndView announlist(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/Contents/announlist.html");
        return  modelAndView;
    }

    @RequestMapping("/sitelist")
    public ModelAndView sitelist(HttpServletRequest request) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/web/sitelist.html");
        return  modelAndView;
    }

    @RequestMapping("/addModel")
    public ModelAndView addModel(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/system/addModel.html");
        return modelAndView;
    }

    @RequestMapping("/addSite")
    public ModelAndView addSite(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/web/addSite.html");
        return  modelAndView;
    }
}
