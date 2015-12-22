package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.common.ArticleSource;
import com.huotu.hotcms.common.ModelType;
import com.huotu.hotcms.entity.Article;
import com.huotu.hotcms.entity.Site;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/16.
 */
@Controller
public class IndexController {

    @RequestMapping({"/index","/"})
    public ModelAndView index(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/View/main.html");
//        String contextPath = request.getContextPath();
//        String serveletPath = request.getServletPath();
//        Site site = new Site();
//        site.setTitle("测试title");
//        site.setCreateTime(LocalDateTime.now());
//        modelAndView.addObject("site",site);
//        File file = new File(".");
//        String path = file.getAbsolutePath();
        return modelAndView;
    }



//    @RequestMapping
//    public ModelAndView index() throws Exception{
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("..html");
////        Site site = new Site();
////        site.setTitle("测试title");
////        site.setCreateTime(LocalDateTime.now());
////        modelAndView.addObject("site",site);
//        return modelAndView;
//    }



    @RequestMapping("test")
    public String testIndex(Site site,Locale locale){
        // 业务层代码 无需关心当前站点以及当前语言的获取！
        Article article = new Article();
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

//    @RequestMapping("{cssPath}")
//    public String main(@PathVariable String cssPath, Model model) {
//        model.addAttribute("backgroundColor", "lightblue");
//        return cssPath;
//    }

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
