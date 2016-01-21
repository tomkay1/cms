/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.controller;


import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by cwb on 2015/12/16.
 */
@Controller
@RequestMapping(value = "/web")
public class IndexController {
    private static final Log log = LogFactory.getLog(IndexController.class);

    @Autowired
    private SiteService siteService;

    @RequestMapping(value = "**")
    public ModelAndView index(HttpServletRequest request)
    {
        log.error("indexController-->"+request.getServletPath());
        ModelAndView modelAndView=new ModelAndView();
//        modelAndView.addObject("customTemplateUrl","http://www.cms.com");
        return modelAndView;
    }

//    @RequestMapping("/remote")
//    public String testRemote(Model model) {
//        model.addAttribute("seo",new Seo("Access remote template success!"));
//        return "/pc/yun-index.html";
//    }
//
//    @RequestMapping("/test")
//    public  String test(Model model){
//        return "/view/test.html";
//    }
//
//    @ModelAttribute("categoryList")
//    public List<Category> getSite() {
//        return new ArrayList<>();
//    }
//
//
//    @RequestMapping("/content/2.html")
//    public String test2(Model model){
//
//        return "/view/test2.html";
//    }


}
