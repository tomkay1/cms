package com.huotu.hotcms.admin.controller.decoration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     页面管理控制器
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/page")
public class PagesController {
    @RequestMapping("/list")
    public ModelAndView widgetTypeList(HttpServletRequest request, @RequestParam("customerid") Integer customerid) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/decoration/pages/list.html");
        return  modelAndView;
    }
}
