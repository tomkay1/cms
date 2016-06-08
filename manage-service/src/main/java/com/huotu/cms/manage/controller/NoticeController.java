package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.service.NoticeService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log log = LogFactory.getLog(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;

    /**
     * 公告列表页面
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/noticeList")
    public ModelAndView noticeList(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        try{
        Notice notice= noticeService.findById(id);
        modelAndView.addObject("notice",notice);
        modelAndView.setViewName("/view/contents/noticeList.html");
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  modelAndView;
    }

    /**
     * 添加公告
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addNotice")
    public ModelAndView addNotice(Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/widget/addNotice.html");
        return  modelAndView;
    }

    /**
     *
     * 修改公告
     * @param id
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateNotice")
    public ModelAndView updateNotice(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/updateNotice.html");
        Notice notice= noticeService.findById(id);
        Category category =notice.getCategory();
        Integer modelType = category.getModelId();
        Set<Category> categorys=categoryRepository.findByCustomerIdAndModelId(customerId,modelType);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("notice",notice);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /**
     *
     * 保存公告
     * @param notice
     * @param categoryId
     * @return
     */
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
                Notice noticeOld = noticeService.findById(notice.getId());
                notice.setCreateTime(noticeOld.getCreateTime());
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
            log.error(ex.getMessage());
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


    /**
     * 获取公告列表
     * @param customerId
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getNoticeList")
    @ResponseBody
    public PageData<NoticeCategory> getNoticeList(@RequestParam(name="customerId",required = false) Integer customerId,
                                         @RequestParam(name="title",required = false) String title,
                                         @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                                         @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<NoticeCategory> pageModel=null;
        try {
             pageModel = noticeService.getPage(customerId, title, page, pageSize);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return pageModel;
    }

    /**
     * 删除(管理员权限)
     * @param id
     * @param customerId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteNotice",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteNotice(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.getCustomerId(request) == customerId) {
                Notice notice = noticeService.findById(id);
                noticeRepository.delete(notice);
//                notice.setDeleted(true);
//                noticeService.saveNotice(notice);
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
