/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

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

/**
 * Created by chendeyu on 2016/1/7.
 */
@Controller
@RequestMapping("/manage/contents")
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

    /**
     * 内容列表页面
     *
     * @param request
     * @param siteId
     * @return
     * @throws Exception
     */
    @RequestMapping("/contentsList")
    public ModelAndView contentsList(HttpServletRequest request,
                                     @RequestParam(name = "siteId", required = false, defaultValue = "0") Long siteId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            long ownerId = Long.valueOf(request.getParameter("ownerId"));
            List<Site> siteList = siteRepository.findByOwner_IdAndDeletedOrderBySiteIdDesc(ownerId, false);
            List<Category> categoryList = new ArrayList<>();
            if (siteList.size() != 0) {
                Site site = siteList.get(0);
                categoryList = categoryRepository.findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(site, false);
            }
            modelAndView.addObject("ownerId", ownerId);
            modelAndView.addObject("siteId", siteId);
            modelAndView.addObject("siteList", siteList);
            modelAndView.addObject("categoryList", categoryList);
            modelAndView.setViewName("/view/contents/contentsList.html");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 添加cms内容
     *
     * @param ownerId
     * @param siteId
     * @param category
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addContents")
    public ModelAndView addContents(long ownerId, Long siteId, Long category) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/contents/addContents.html");
            List<Category> categories;
            if (category == -1) {
                categories = categoryRepository.findBySite_Owner_IdAndSite_SiteIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(ownerId, siteId, false);
            } else {
                categories = categoryService.findByParentIdsLike(category.toString());
            }
            int size = categories.size();
            modelAndView.addObject("size", size);
            modelAndView.addObject("categorys", categories);
            modelAndView.addObject("ownerId", ownerId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 内容页面条件选择框
     *
     * @param siteId
     * @return
     * @throws Exception
     */
    @RequestMapping("/contentsSelect")
    @ResponseBody
    public List<SiteCategory> contentsSelect(@RequestParam(name = "siteId", defaultValue = "0") Long siteId) throws Exception {
        Site site = siteRepository.findOne(siteId);
        List<Category> categoryList = categoryRepository.findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(site, false);
        List<SiteCategory> siteCategoryList = new ArrayList<>();
        for (Category category : categoryList) {
            SiteCategory siteCategory = new SiteCategory();
            siteCategory.setCategoryName(category.getName());
            siteCategory.setCategoryId(category.getId());
            siteCategoryList.add(siteCategory);
        }
        return siteCategoryList;
    }


    /**
     * 获取内容列表详细信息
     *
     * @param title
     * @param siteId
     * @param category
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getContentsList")
    @ResponseBody
    public PageData<Contents> getContentsList(
            @RequestParam(name = "name", required = false) String title,
            @RequestParam(name = "siteId", required = false) Long siteId,
            @RequestParam(name = "category", required = false) Long category,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pagesize", defaultValue = "20") int pageSize) {

        return contentsService.getPage(title, siteId, category, page, pageSize);
    }
}

