package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.entity.Site;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/16.
 */
@Controller
public class IndexController {

    @RequestMapping
    public ModelAndView index() throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        Site site = new Site();
        site.setTitle("测试title");
        site.setCreateTime(LocalDateTime.now());
        modelAndView.addObject("site",site);
        return modelAndView;
    }

    @RequestMapping("test")
    public String testIndex(Site site,Locale locale){
        // 业务层代码 无需关心当前站点以及当前语言的获取！

        return "";
    }

    @RequestMapping("content.html")
    public String content(Model model) {
        model.addAttribute("value1","变量1");
        model.addAttribute("value2","变量2");
        return "content.html";
    }

    @RequestMapping(value = "common.js", method = RequestMethod.GET)
    public String common(Model model) {
        model.addAttribute("code", "Thymeleaf rules!".hashCode());
        return "common.js";
    }

    @RequestMapping(value = "main.css", method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("backgroundColor", "lightblue");
        return "main.css";
    }

    @RequestMapping("confirm.js")
    public ModelAndView js() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("confirm.js");
        return modelAndView;
    }

    @ModelAttribute("site2")
    public Site se() {
        Site site = new Site();
        site.setTitle("测试title……");
        return site;
    }
}
