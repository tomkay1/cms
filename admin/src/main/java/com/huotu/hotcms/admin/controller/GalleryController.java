package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.service.StaticResourceService;
import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.GalleryService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
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
 * Created by chendeyu on 2016/1/10.
 */
@Controller
@RequestMapping("/gallery")
public class GalleryController {

//        @Autowired
//        private LinkService linkService;

    @Autowired
    private GalleryService galleryService;
//
    @Autowired
    private StaticResourceService resourceServer;
//        @Autowired
//        private SiteRepository siteRepository;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private CookieUser cookieUser;


        @RequestMapping("/galleryList")
        public ModelAndView galleryList(@RequestParam(value = "id",defaultValue = "0") Long id) throws Exception
        {
            ModelAndView modelAndView=new ModelAndView();
            modelAndView.setViewName("/view/contents/galleryList.html");
            Gallery gallery= galleryService.findById(id);
            String logo_uri="";
            if(!StringUtils.isEmpty(gallery.getThumbUri())) {
                logo_uri = resourceServer.getResource(gallery.getThumbUri()).toString();
            }
            modelAndView.addObject("logo_uri",logo_uri);
            modelAndView.addObject("gallery", gallery);
            return modelAndView;
        }



        @RequestMapping(value = "/addGallery")
        public ModelAndView addGallery(Integer customerId) throws Exception{
            ModelAndView modelAndView=new ModelAndView();
            modelAndView.setViewName("/view/widget/addGallery.html");
            return  modelAndView;
        }

        /*
      * 修改图库
      * */
        @RequestMapping("/updateGallery")
        public ModelAndView updateGallery(@RequestParam(value = "id",defaultValue = "0") Long id,Integer customerId) throws Exception{
            ModelAndView modelAndView=new ModelAndView();
            modelAndView.setViewName("/view/contents/updateGallery.html");
            Gallery gallery= galleryService.findById(id);
            String logo_uri="";
            if(!StringUtils.isEmpty(gallery.getThumbUri())) {
                logo_uri = resourceServer.getResource(gallery.getThumbUri()).toString();
            }
            Category category =gallery.getCategory();
            Integer modelType = category.getModelId();
            Set<Category> categorys=categoryRepository.findByCustomerIdAndModelId(customerId, modelType);
            modelAndView.addObject("logo_uri",logo_uri);
            modelAndView.addObject("categorys",categorys);
            modelAndView.addObject("gallery",gallery);
            return modelAndView;
        }


        /*
          * 更新链接
          * */
        @RequestMapping(value = "/saveGallery",method = RequestMethod.POST)
        @Transactional(value = "transactionManager")
        @ResponseBody
        public ResultView saveLink(Gallery gallery,Long categoryId){
            ResultView result=null;
            try {
                Long id = gallery.getId();
                Category category = categoryRepository.getOne(categoryId);
                if(id!=null)
                {
                    Gallery galleryOld = galleryService.findById(gallery.getId());
                    gallery.setCreateTime(galleryOld.getCreateTime());
                    gallery.setUpdateTime(LocalDateTime.now());
                }
                else{
                    gallery.setCreateTime(LocalDateTime.now());
                    gallery.setUpdateTime(LocalDateTime.now());
                }
                gallery.setCategory(category);
                galleryService.saveGallery(gallery);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            }
            catch (Exception ex)
            {
                result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
            }
            return  result;
        }




        @RequestMapping(value = "/deleteGallery",method = RequestMethod.POST)
        @ResponseBody
        public ResultView deleteGallery(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
            ResultView result=null;
            try{
                if(cookieUser.isSupper(request)) {
                    Gallery gallery = galleryService.findById(id);
                    gallery.setDeleted(true);
                    galleryService.saveGallery(gallery);
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
