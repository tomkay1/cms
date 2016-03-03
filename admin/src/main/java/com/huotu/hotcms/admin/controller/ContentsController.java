package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.Contents;
import com.huotu.hotcms.service.model.SiteCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.ContentsService;
import com.huotu.hotcms.service.util.PageData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private static final Log log = LogFactory.getLog(ContentsController.class);

    @Autowired
    SiteRepository siteRepository;


    @Autowired
    ContentsService contentsService;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping("/contentsList")
    public ModelAndView contentsList(HttpServletRequest request,
                                     @RequestParam(name = "siteId",required = false,defaultValue = "0")Long siteId) throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        try{
            Integer customerId =Integer.valueOf(request.getParameter("customerid"));
            List<Site> siteList =siteRepository.findByCustomerIdAndDeletedOrderBySiteIdDesc(customerId,false);
            List<Category> categoryList =new ArrayList<>();
            if(siteList.size()!=0){
                Site site=siteList.get(0);
                categoryList= categoryRepository.findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(site, false);
            }
            modelAndView.addObject("customerId",customerId);
            modelAndView.addObject("siteId",siteId);
            modelAndView.addObject("siteList", siteList);
            modelAndView.addObject("categoryList", categoryList);
            modelAndView.setViewName("/view/contents/contentsList.html");
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
        }

    @RequestMapping(value = "/addContents")
    public ModelAndView addContents(Integer customerId,Long siteId,Long category) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/addContents.html");
        List<Category> categorys = new ArrayList<>();
        if(category==-1){
            categorys=categoryRepository.findByCustomerIdAndSite_SiteIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(customerId, siteId, false);
        }
        else{
//           categorys=categoryRepository.findByCustomerIdAndSite_SiteIdAndIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(customerId, siteId, category, false);
            categorys=categoryService.findByParentIdsLike(category.toString());
        }
        int size =categorys.size();
        modelAndView.addObject("size",size);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("customerId",customerId);
        }catch (Exception ex){
                log.error(ex.getMessage());
        }
            return modelAndView;
        }

    //���ı�վ��ʱ����Ŀ�Զ��仯
    @RequestMapping("/contentsSelect")
    @ResponseBody
    public List<SiteCategory> contentsSelect(HttpServletRequest request,
                                       @RequestParam(name = "siteId",required = true,defaultValue = "0")Long siteId) throws Exception {
        Integer customerId =Integer.valueOf(request.getParameter("customerid"));
        Set<Site> siteList =siteRepository.findByCustomerIdAndDeleted(customerId, false);
        Site site = siteRepository.findOne(siteId);
        List<Category> categoryList =categoryRepository.findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(site, false);
        List<SiteCategory> siteCategoryList = new ArrayList<>();
        for(Category category:categoryList){
            SiteCategory siteCategory = new SiteCategory();
            siteCategory.setCategoryName(category.getName());
            siteCategory.setCategoryId(category.getId());
            siteCategoryList.add(siteCategory);
        }
        return  siteCategoryList;
    }

    @RequestMapping(value = "/getContentsList")
    @ResponseBody
    public PageData<Contents> getContentsList(
                                       @RequestParam(name="name",required = false) String title,
                                       @RequestParam(name="siteId",required = false) Long siteId,
                                       @RequestParam(name="category",required = false) Long category,
                                       @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                       @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<Contents> getContentsList = contentsService.getPage(title,siteId,category,page,pageSize);

        return getContentsList;
    }
}

