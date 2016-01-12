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

    @RequestMapping
    public ModelAndView index()
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("customTemplateUrl","http://www.cms.com");
        return modelAndView;
    }
    

}
