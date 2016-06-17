/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.GalleryListService;
import com.huotu.hotcms.service.service.GalleryService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2016/1/10.
 */
@Controller
@RequestMapping("/manage/gallery")
public class GalleryController {
    private static final Log log = LogFactory.getLog(GalleryController.class);

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryListService galleryListService;

    @Autowired
    private StaticResourceService resourceServer;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CookieUser cookieUser;


    @RequestMapping("/galleryList")
    public ModelAndView galleryList(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/galleryList.html");
            Gallery gallery = galleryService.findById(id);
            String logo_uri = "";
            if (!StringUtils.isEmpty(gallery.getThumbUri())) {
                logo_uri = resourceServer.getResource(gallery.getThumbUri()).toString();
            }
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("gallery", gallery);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    @RequestMapping("/galleryListDetail")
    public ModelAndView galleryListDetail(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/galleryListDetail.html");
            modelAndView.addObject("id", id);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getGalleryList")
    @ResponseBody
    public PageData<GalleryList> getModelList(@RequestParam(name = "ownerId") long ownerId,
                                              @RequestParam(name = "galleryId") Long galleryId,
                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "pagesize", defaultValue = "20") int pageSize) {
        PageData<GalleryList> pageModel = null;
        try {
            Page<GalleryList> galleryServicePage = galleryListService.getPage(ownerId, galleryId, page, pageSize);
            if (galleryServicePage != null) {//先取得分页page再进行转化
                List<GalleryList> galleryLists = galleryServicePage.getContent();
                for (GalleryList gallery : galleryLists) {
                    gallery.setThumbUri(resourceServer.getResource(gallery.getThumbUri()).toString());
                    gallery.setGallery(null);
                }
                pageModel = new PageData<>();
                pageModel.setPageCount(galleryServicePage.getTotalPages());
                pageModel.setPageIndex(galleryServicePage.getNumber());
                pageModel.setPageSize(galleryServicePage.getSize());
                pageModel.setTotal(galleryServicePage.getTotalElements());
                pageModel.setRows(galleryServicePage.getContent()
                        .toArray(new GalleryList[galleryServicePage.getContent().size()]));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return pageModel;
    }


    @RequestMapping(value = "/addGallery")
    public ModelAndView addGallery() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/widget/addGallery.html");
        return modelAndView;
    }

    @RequestMapping(value = "/addGalleryList")
    public ModelAndView addGalleryList(Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("galleryId", id);
        modelAndView.setViewName("/view/contents/addGalleryList.html");
        return modelAndView;
    }

    /**
     * 修改图库
     */
    @RequestMapping("/updateGallery")
    public ModelAndView updateGallery(@RequestParam(value = "id", defaultValue = "0") Long id, long ownerId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/updateGallery.html");
            Gallery gallery = galleryService.findById(id);
            String logo_uri = "";
            if (!StringUtils.isEmpty(gallery.getThumbUri())) {
                logo_uri = resourceServer.getResource(gallery.getThumbUri()).toString();
            }
            Category category = gallery.getCategory();
            Integer modelType = category.getModelId();
            Set<Category> categorys = categoryRepository.findBySite_Owner_IdAndModelId(ownerId, modelType);
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("categorys", categorys);
            modelAndView.addObject("gallery", gallery);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 修改图库
     */
    @RequestMapping("/updateGalleryList")
    public ModelAndView updateGalleryList(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/updateGalleryList.html");
            GalleryList galleryList = galleryListService.findGalleryListById(id);
            String logo_uri = "";
            if (!StringUtils.isEmpty(galleryList.getThumbUri())) {
                logo_uri = resourceServer.getResource(galleryList.getThumbUri()).toString();
            }
            modelAndView.addObject("logo_uri", logo_uri);
            modelAndView.addObject("galleryList", galleryList);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }


    /*
      * 保存图库
      * */
    @RequestMapping(value = "/saveGallery", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveGallery(Gallery gallery, Long categoryId) {
        ResultView result = null;
        try {
            Long id = gallery.getId();
            Category category = categoryRepository.getOne(categoryId);
            if (id != null) {
                Gallery galleryOld = galleryService.findById(gallery.getId());
                gallery.setCreateTime(galleryOld.getCreateTime());
                gallery.setUpdateTime(LocalDateTime.now());
            } else {
                gallery.setCreateTime(LocalDateTime.now());
                gallery.setUpdateTime(LocalDateTime.now());
            }
            gallery.setCategory(category);
            galleryService.saveGallery(gallery);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    /*
         * 保存图片
         * */
    @RequestMapping(value = "/saveGalleryList", method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView saveGalleryList(GalleryList galleryList, Long galleryId, String wide, String height) {
        ResultView result = null;
        try {
            Long id = galleryList.getId();
            if (id == null) {
                galleryList.setCreateTime(LocalDateTime.now());
            } else {
                galleryList.setCreateTime(galleryListService.findGalleryListById(id).getCreateTime());
                galleryList.setUpdateTime(LocalDateTime.now());
                if (wide.equals("")) {
                    galleryList.setSize(galleryListService.findGalleryListById(id).getSize());
                }
            }
            if (!wide.equals("")) {
                galleryList.setSize(wide + "*" + height);
            }
            galleryList.setGallery(galleryService.findById(galleryId));
            galleryListService.saveGalleryList(galleryList);
            result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }


    @RequestMapping(value = "/deleteGallery", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteGallery(@RequestParam(name = "id", required = true, defaultValue = "0") Long id
            , long ownerId, HttpServletRequest request) {
        ResultView result = null;
        try {
            if (cookieUser.getOwnerId(request) == ownerId) {
                Gallery gallery = galleryService.findById(id);
                gallery.setDeleted(true);
                galleryService.saveGallery(gallery);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    @RequestMapping(value = "/deleteGalleryList", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteGalleryList(@RequestParam(name = "id", required = true, defaultValue = "0") Long id
            , long ownerId, HttpServletRequest request) {
        ResultView result;
        try {
            if (cookieUser.getOwnerId(request) == ownerId) {
                GalleryList galleryList = galleryListService.findGalleryListById(id);
                galleryList.setDeleted(true);
                galleryListService.saveGalleryList(galleryList);
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }


}
