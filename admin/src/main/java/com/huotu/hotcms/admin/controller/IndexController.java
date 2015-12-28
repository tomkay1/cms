package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.model.Seo;
import com.huotu.hotcms.entity.Site;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cwb on 2015/12/16.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String testIndex(Site site){
        String viewName = "/view/index.html";
        if(site.isCustom()) {
            viewName = site.getCustomTemplateUrl();
        }
        return viewName;
    }

    @RequestMapping("/*")
    public String testIndex2(Site site) throws Exception {
        return "/view/index.html";
    }


    @RequestMapping("/remote")
    public String testRemote(Model model) {
        model.addAttribute("seo",new Seo("Access remote template success!"));
        return "/pc/yun-index.html";
    }

}
