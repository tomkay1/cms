package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.service.StaticResourceService;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.model.LinkCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.LinkService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private static final Log log = LogFactory.getLog(LinkController.class);

    @Autowired
    private LinkService linkService;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;


    @RequestMapping("/linkList")
    public ModelAndView linkList(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/linkList.html");
        Link link= linkService.findById(id);
        String logo_uri="";
        if(!StringUtils.isEmpty(link.getThumbUri())) {
            logo_uri = resourceServer.getResource(link.getThumbUri()).toString();
        }
        modelAndView.addObject("logo_uri",logo_uri);
        modelAndView.addObject("link", link);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }




    @RequestMapping(value = "/addLink")
    public ModelAndView addLink(Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/widget/addLink.html");
        return  modelAndView;
    }

    /*
  * 修改链接
  * */
    @RequestMapping("/updateLink")
    public ModelAndView updateLink(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/updateLink.html");
        Link link= linkService.findById(id);
        String logo_uri="";
        if(!StringUtils.isEmpty(link.getThumbUri())) {
            logo_uri = resourceServer.getResource(link.getThumbUri()).toString();
        }
        Category category =link.getCategory();
        Integer modelType = category.getModelId();
        Set<Category> categorys=categoryRepository.findByCustomerIdAndModelId(customerId,modelType);
        modelAndView.addObject("logo_uri",logo_uri);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("link",link);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
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
            log.error(ex.getMessage());
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
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


}