package com.huotu.cms.manage.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Video;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.VideoRepository;
import com.huotu.hotcms.service.service.VideoService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
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
 * Created by chendeyu on 2016/1/11.
 */
@Controller
@RequestMapping("/video")
public class VideoController {
    private static final Log log = LogFactory.getLog(SiteController.class);
    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CookieUser cookieUser;

    /**
     * 视频列表
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/videoList")
    public ModelAndView videoList(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/videoList.html");
        Video video= videoService.findById(id);
        String logo_uri="";
        if(!StringUtils.isEmpty(video.getThumbUri())) {
            logo_uri = resourceServer.getResource(video.getThumbUri()).toString();
        }
        modelAndView.addObject("logo_uri",logo_uri);
        modelAndView.addObject("video",video);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /**
     * 添加视频信息页面
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addVideo")
    public ModelAndView addVideo(Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/widget/addVideo.html");
        return  modelAndView;
    }

    /**
     *
     * 修改视频
     * @param id
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateVideo")
    public ModelAndView updateVideo(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        try{
        modelAndView.setViewName("/view/contents/updateVideo.html");
        Video video= videoService.findById(id);
        String logo_uri="";
        if(!StringUtils.isEmpty(video.getThumbUri())) {
            logo_uri = resourceServer.getResource(video.getThumbUri()).toString();
        }
        Category category =video.getCategory();
        Integer modelType = category.getModelId();
        Set<Category> categorys=categoryRepository.findByCustomerIdAndModelId(customerId, modelType);
        modelAndView.addObject("logo_uri",logo_uri);
        modelAndView.addObject("categorys",categorys);
        modelAndView.addObject("video",video);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /**
     * 保存video
     * @param video
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/saveVideo",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveVideo(Video video,Long categoryId){
        ResultView result=null;
        try {
            Long id = video.getId();
            Category category = categoryRepository.getOne(categoryId);
            if(id!=null)
            {
                Video videoOld = videoService.findById(video.getId());
                video.setCreateTime(videoOld.getCreateTime());
                video.setUpdateTime(LocalDateTime.now());
            }
            else{
                video.setCreateTime(LocalDateTime.now());
                video.setUpdateTime(LocalDateTime.now());
            }
            video.setCategory(category);
            videoService.saveVideo(video);
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
     * 删除video
     * @param id
     * @param customerId
     * @param request
     * @return
     */

    @RequestMapping(value = "/deleteVideo",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteVideo(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
        try{
            if(cookieUser.getCustomerId(request) == customerId) {
                Video video = videoService.findById(id);
                videoRepository.delete(video);
//                video.setDeleted(true);
//                videoService.saveVideo(video);
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
