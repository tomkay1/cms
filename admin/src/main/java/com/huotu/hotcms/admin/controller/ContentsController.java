package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.SiteCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2016/1/7.
 */
@Controller
@RequestMapping("/contents")
public class ContentsController {

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping("/contentsList")
    public ModelAndView contentsList(HttpServletRequest request) throws Exception
    {
        Integer customerId =Integer.valueOf(request.getParameter("customerid"));
        Set<Site> siteSet =siteRepository.findByCustomerId(customerId);
        Set<Category> categorySet =categoryRepository.findByCustomerId(customerId);
        List<SiteCategory> siteCategoryList =new ArrayList<>();
        SiteCategory siteCategory = new SiteCategory();
        for(Site site : siteSet){
           for(Category category :categorySet){
               if(category.getSite()==site){
                   siteCategory.setSiteId(site.getSiteId());
                   siteCategory.setCategoryId(category.getId());
                   siteCategory.setSiteName(site.getName());
                   siteCategory.setCategoryName(category.getName());
                   siteCategoryList.add(siteCategory);
               }
           }
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("siteCategoryList", siteCategoryList);
        modelAndView.setViewName("/view/contents/contentsList.html");

        return  modelAndView;
    }
}
