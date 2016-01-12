package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.Contents;
import com.huotu.hotcms.service.model.SiteCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.ModelService;
import com.huotu.hotcms.service.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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
    ModelService modelService;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping("/contentsList")
    public ModelAndView contentsList(HttpServletRequest request,
                                     @RequestParam(name = "siteId",required = false,defaultValue = "0")Long siteId) throws Exception {

        Integer customerId =Integer.valueOf(request.getParameter("customerid"));
        Set<Site> siteSet =siteRepository.findByCustomerId(customerId);
        Site site = siteRepository.findOne(siteId);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("customerId",customerId);
        modelAndView.addObject("siteSet", siteSet);
        modelAndView.setViewName("/view/contents/contentsList.html");
        return  modelAndView;
    }

    @RequestMapping(value = "/addContents")
    public ModelAndView addContents(Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/addContents.html");
        Set<Category> categorys=categoryRepository.findByCustomerId(customerId);
//        List<Region> regions =regionRepository.findAll();
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("customerId",customerId);
        return  modelAndView;
    }

    @RequestMapping("/contentsSelect")
    @ResponseBody
    public List<SiteCategory> contentsSelect(HttpServletRequest request,
                                       @RequestParam(name = "siteId",required = true,defaultValue = "0")Long siteId) throws Exception {
        Integer customerId =Integer.valueOf(request.getParameter("customerid"));
        Set<Site> siteSet =siteRepository.findByCustomerId(customerId);
        Site site = siteRepository.findOne(siteId);
        List<Category> categoryList =categoryRepository.findBySiteOrderById(site);
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
    public PageData<Contents> getContentsList(@RequestParam(name="customerId",required = false) Integer customerId,
                                       @RequestParam(name="name",required = false) String name,
                                       @RequestParam(name="siteId",required = false) Long siteId,
                                       @RequestParam(name="category",required = false) Long category,
                                       @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                       @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<Contents> data = null;
       List<Object[]> contentsList = linkRepository.findAllContents(siteId);
        List<Contents> contentsList1 = new ArrayList<>();
        for(Object[] o : contentsList) {
            Contents contents = new Contents();
            contents.setTitle((String)o[0]);
            contents.setDescription((String)o[1]);
            contents.setName((String) o[2]);
            contents.setId((Long) o[3]);
            Integer modelId=(Integer) o[4];
            if(modelId!=null) {
                contents.setModel(ModelType.valueOf(modelId).toString().toLowerCase());
                contents.setModelname(ModelType.valueOf(modelId).getValue().toString());
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
            String str = df.format(o[5]);
            contents.setCreateTime((String) str);
            contentsList1.add(contents);
        }
        data = new PageData<Contents>();
//        data.setPageCount(pageData.getTotalPages());
//        data.setPageIndex(pageData.getNumber());
//        data.setPageSize(pageData.getSize());
//        data.setTotal(pageData.getTotalElements());
        data.setRows((Contents[])contentsList1.toArray(new Contents[contentsList1.size()]));
        return data;
    }
}
