/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.controller;


import com.huotu.hotcms.service.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by cwb on 2015/12/16.
 */
@Controller
public class IndexController {

    @Autowired
    private SiteService siteService;

    @RequestMapping({"/**","/",""})
    public ModelAndView index()
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("customTemplateUrl","http://www.cms.com");
        return modelAndView;
    }

//    @RequestMapping("/")
//    public String testIndex(Site site,Model model){
//        Set<Site> siteSet = siteService.getSite(3447);
//        model.addAttribute("site",site);
//        model.addAttribute("siteList",siteSet);
//        String viewName = "/view/index.html";
//        if(site.isCustom()) {
//            viewName = site.getCustomTemplateUrl();
//        }
//        return viewName;
//    }
//
//
//    @RequestMapping("/test4")
//    public ModelAndView testIndex4()
//    {
//        ModelAndView modelAndView=new ModelAndView();
//        modelAndView.setViewName("http://www.cms.com/content.html");
//        return  modelAndView;
//    }
//
//    @RequestMapping("/*/*")
//    public ModelAndView index(){
//        ModelAndView modelAndView=new ModelAndView();
////        modelAndView.setViewName("/view/system/regionList.html");
//        return  modelAndView;
//    }

//
//    @RequestMapping("/**")
//    public String testIndex2(Site site,Model model) throws Exception {
//        model.addAttribute("site",site);
//        String viewName = "/view/index.html";
//        if(site.isCustom()) {
//            viewName = site.getCustomTemplateUrl();
//        }
//        return viewName;
//    }

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
