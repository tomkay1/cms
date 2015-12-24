package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/site")
public class siteController {

    @Autowired
    private SiteService siteService;

    @RequestMapping("/sitelist")
    public ModelAndView sitelist(HttpServletRequest request,Site site) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/web/sitelist.html");
        return  modelAndView;
    }



    @RequestMapping("/addSite")
    public ModelAndView addSite(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/View/web/addSite.html");
//        Site site = new Site();
//        site.setKeywords("1");
//        site.setCustomerId(1);
//        site.setId(Long.valueOf(1));
//        siteService.modifySite(site);
        return  modelAndView;
    }
}
