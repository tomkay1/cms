package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.model.Seo;
import com.huotu.hotcms.entity.Site;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2015/12/16.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String testIndex(Site site){
        // 业务层代码 无需关心当前站点以及当前语言的获取！
        return "redirect:/f";
    }


    @RequestMapping("/remote")
    public String testRemote(Model model) {
        model.addAttribute("seo",new Seo("Access remote template success!"));
        return "pc/yun-index.html";
    }

    @RequestMapping("/f")
    public String index(Site site) {
        return "view/index.html";
    }

    @RequestMapping("/f/{region}")
    public String testIndex2(Site site,@PathVariable String region) {
        return "view/index.html";
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
