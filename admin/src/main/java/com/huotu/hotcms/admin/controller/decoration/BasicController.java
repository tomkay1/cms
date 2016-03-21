package com.huotu.hotcms.admin.controller.decoration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     基础页装修控制器
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/basic")
public class BasicController {
    @RequestMapping("/{name}")
    public ModelAndView widgetTypeList(HttpServletRequest request, @RequestParam("customerid") Integer customerid,@RequestParam("siteId") String siteId, @PathVariable("name") String name) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        String[] goods = {"dd","ss","zz"};
        modelAndView.addObject("goods",goods);
        modelAndView.setViewName(String.format("%s_%s.shtml",siteId,name));
        return  modelAndView;
    }

    @RequestMapping("/edit")
    public ModelAndView editMain(HttpServletRequest request, @RequestParam("customerid") Integer customerid,@RequestParam("siteId") String siteId,@RequestParam("url") String url) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("url",url+"?customerid="+customerid+"&siteId="+siteId);
        modelAndView.setViewName("/decoration/edit/editMain.html");
        return  modelAndView;
    }

    @RequestMapping("/test")
    public ModelAndView test(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/test.html");

        return  modelAndView;
    }
}
