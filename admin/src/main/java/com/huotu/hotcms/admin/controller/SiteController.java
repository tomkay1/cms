package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.service.SiteService;
import com.huotu.hotcms.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @RequestMapping("/sitelist")
    public ModelAndView sitelist(HttpServletRequest request,Site site) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/sitelist.html");

        return  modelAndView;
    }



    @RequestMapping(value = "/addSite")
    public ModelAndView addSite(HttpServletRequest request,Site site) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/addSite.html");
//        Site site = new Site();
//        site.setKeywords("1");
//        site.setCustomerId(1);
//        site.setId(Long.valueOf(1));
//        siteService.modifySite(site);
        return  modelAndView;
    }

    @RequestMapping(value = "/saveSite",method = RequestMethod.POST)
    public ModelAndView saveSite(HttpServletRequest request,String... hosts) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/sitelist.html");
        siteService.addSite(request,hosts);
        return modelAndView;
    }

    @RequestMapping(value = "/getSiteList")
    @ResponseBody
    public PageData<Site> getModelList(@RequestParam(name="name",required = false) String name,
                           @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                           @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<Site> pageModel=siteService.getPage(name, page, pageSize);
        return pageModel;
    }
}
