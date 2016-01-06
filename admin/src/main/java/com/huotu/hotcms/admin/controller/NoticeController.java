package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.NoticeService;
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
 * Created by chendeyu on 2016/1/5.
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
//
//    @Autowired
//    private HostService hostService;
//
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;

    @RequestMapping("/noticeList")
    public ModelAndView noticeList(HttpServletRequest request) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/noticeList.html");

        return  modelAndView;
    }



    @RequestMapping(value = "/addNotice")
    public ModelAndView addNotice(HttpServletRequest request,Integer customerid) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/contents/addNotice.html");
        Set<Category> categorys=categoryRepository.findByCustomerId(customerid);
//        List<Region> regions =regionRepository.findAll();
        modelAndView.addObject("categorys",categorys);
        return  modelAndView;
    }

    /*
  * 修改栏目
  * */
    @RequestMapping("/updateCategory")
    public ModelAndView updateCategory(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/notice/updateNotice.html");
        Notice notice= noticeService.findById(id);
        Set<Category> categorys=categoryRepository.findByCustomerId(customerId);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("notice",notice);
        return modelAndView;
    }


    /*
      * 更新公告
      * */
    @RequestMapping(value = "/saveNotice",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveNotice(Notice notice,Long categoryId){
        ResultView result=null;
        try {
            Long id = notice.getId();
            Category category = categoryRepository.getOne(categoryId);
            if(id!=null)
            {
                notice.setUpdateTime(LocalDateTime.now());
            }
            else{
                notice.setCreateTime(LocalDateTime.now());
                notice.setUpdateTime(LocalDateTime.now());
            }

            notice.setCategory(category);
            noticeService.saveNotice(notice);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }



//    @RequestMapping("/updateNotice")
//    public ModelAndView updateNotice(@RequestParam(value = "id",defaultValue = "0") Long id,int customerId) throws Exception{
//        ModelAndView modelAndView=new ModelAndView();
//        modelAndView.setViewName("/view/web/updateNotice.html");
//
//        if(id!=0) {
//            Notice notice = noticeService.findByNoticeIdAndCustomerId(id, customerId);
//            if (notice != null) {
//                modelAndView.addObject("notice", notice);
//                Set<Host> hosts =notice.getHosts();
//                String domains="";
//                for(Host host : hosts){
//                    String domain= host.getDomain();
//                    domains=domains+domain+",";
//                }
//                Region region= notice.getRegion();
//                modelAndView.addObject("region",region);
//                modelAndView.addObject("domains", domains.substring(0, domains.length() - 1));
//            }
//        }
//        return modelAndView;
//    }

    @RequestMapping(value = "/getNoticeList")
    @ResponseBody
    public PageData<NoticeCategory> getNoticeList(@RequestParam(name="customerId",required = false) Integer customerId,
                                         @RequestParam(name="title",required = false) String title,
                                         @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                         @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<NoticeCategory> pageModel=noticeService.getPage(customerId,title, page, pageSize);
        return pageModel;
    }

    @RequestMapping(value = "/deleteNotice",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteModel(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.isSupper(request)) {
                Notice notice = noticeService.findById(id);
                notice.setDeleted(true);
                noticeService.saveNotice(notice);
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
