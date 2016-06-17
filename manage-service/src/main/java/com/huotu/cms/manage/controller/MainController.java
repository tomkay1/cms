/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 视图控制器
 *
 * @author xhl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/manage/main")
public class MainController {

    @RequestMapping({"/index", ""})
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/main.html");
        return modelAndView;
    }

    @RequestMapping(value = "/decorated")
    public ModelAndView decorated(String scope) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scope", scope);
        modelAndView.setViewName("/decoration/decorated.html");
        return modelAndView;
    }
}
