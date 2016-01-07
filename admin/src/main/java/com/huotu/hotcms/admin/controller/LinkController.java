package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by chendeyu on 2016/1/6.
 */
@Controller
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;
    //
//    @Autowired
//    private HostService hostService;
//
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;


    @RequestMapping("/linkList")
    public ModelAndView linkList(HttpServletRequest request) throws Exception
    {
        Integer customerId =Integer.valueOf(request.getParameter("customerid"));
        Set<Site> siteSet =siteRepository.findByCustomerId(customerId);
        Set<Category> categorySet =categoryRepository.findByCustomerId(customerId);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("siteSet",siteSet);
        modelAndView.addObject("categorySet",categorySet);
        modelAndView.setViewName("/view/contents/linkList.html");

        return  modelAndView;
    }





    @RequestMapping(value = "/addLink")
    public ModelAndView addLink(HttpServletRequest request,Integer customerid) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/addLink.html");
        Set<Category> categorys=categoryRepository.findByCustomerId(customerid);
//        List<Region> regions =regionRepository.findAll();
        modelAndView.addObject("categorys",categorys);
        return  modelAndView;
    }

    /*
  * 修改栏目
  * */
    @RequestMapping("/updateLink")
    public ModelAndView updateLink(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/updateLink.html");
        Link link= linkService.findById(id);
        Set<Category> categorys=categoryRepository.findByCustomerId(customerId);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("link",link);
        return modelAndView;
    }


    /*
      * 更新链接
      * */
    @RequestMapping(value = "/saveLink",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveLink(Link link,Long categoryId){
        ResultView result=null;
        try {
            Long id = link.getId();
            Category category = categoryRepository.getOne(categoryId);
            if(id!=null)
            {
                Link linkOld = linkService.findById(link.getId());
                link.setCreateTime(linkOld.getCreateTime());
                link.setUpdateTime(LocalDateTime.now());
            }
            else{
                link.setCreateTime(LocalDateTime.now());
                link.setUpdateTime(LocalDateTime.now());
            }
            link.setCategory(category);
            linkService.saveLink(link);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


    @RequestMapping(value = "/getLinkList")
    @ResponseBody
    public PageData<LinkCategory> getLinkList(@RequestParam(name="customerId",required = false) Integer customerId,
                                                    @RequestParam(name="title",required = false) String title,
                                                    @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                                    @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<LinkCategory> pageModel=linkService.getPage(customerId,title, page, pageSize);
        return pageModel;
    }

    @RequestMapping(value = "/deleteLink",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteLink(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                Link link = linkService.findById(id);
                link.setDeleted(true);
                linkService.saveLink(link);
                result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


}