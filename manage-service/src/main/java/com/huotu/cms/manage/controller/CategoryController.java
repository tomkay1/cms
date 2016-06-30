/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.SiteManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.cms.manage.util.web.CookieUser;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.EnumUtils;
import com.huotu.hotcms.service.common.ModelType;
import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.converter.CategoryFormatter;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.CategoryTreeModel;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryController extends SiteManageController<Category, Long, Void, Void> {
    private static final Log log = LogFactory.getLog(CategoryController.class);


    @Autowired
    CategoryService categoryService;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    SiteService siteService;
    @Autowired
    RouteService routeService;
    @Autowired
    private CookieUser cookieUser;
    @Autowired
    private CategoryFormatter categoryFormatter;

    /**
     * 栏目列表视图
     */
    @RequestMapping("/categoryList")
    public ModelAndView categoryList() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/section/categoryList.html");
        return modelAndView;
    }

    /**
     * 根据商户ID获得站点列表业务API xhl
     */
    @RequestMapping("/getSiteList")
    @ResponseBody
    public ResultView getSiteList(@RequestParam(value = "ownerId") long ownerId) {
        ResultView result;
        try {
            Set<Site> sites = siteService.findByOwnerIdAndDeleted(ownerId, false);
            if (sites != null && sites.size() > 0) {
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), sites.toArray(new Site[sites.size()]));
            } else {
                result = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return result;
    }

    /**
     * 获得栏目列表 xhl
     */
    @RequestMapping("/getCategoryList")
    @ResponseBody
    public ResultView getCategoryList(@RequestParam(name = "siteId", required = false) Long siteId,
                                      @RequestParam(name = "name", required = false) String name) {
        ResultView resultView;
        try {
            Site site = siteService.getSite(siteId);
            List<Category> categoryList = categoryService.getCategoryBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(site, false, name);
            List<CategoryTreeModel> categoryTreeModelList = categoryService.ConvertCategoryTreeByCategory(categoryList);
            categoryTreeModelList = CategoryTreeModel.setEmptyCategoryTreeModel(categoryTreeModelList);
            resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), categoryTreeModelList);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return resultView;
    }

    /**
     * 增加栏目
     */
    @RequestMapping("/addCategory")
    public ModelAndView addCategory(@RequestParam(value = "siteId") Long siteId,
                                    @RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("/view/section/addCategory.html");
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                Site site = siteService.getSite(siteId);
                modelAndView.addObject("site", site);
            } else {
                modelAndView.addObject("site", category.getSite());
            }
            modelAndView.addObject("modelTypes", ModelType.values());
            modelAndView.addObject("routeTypes", RouteType.values());
            modelAndView.addObject("category", category);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return modelAndView;
    }

    /**
     * 修改栏目
     */
    @RequestMapping("/updateCategory")
    public ModelAndView updateCategory(@RequestParam(value = "id", defaultValue = "0") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/view/section/updateCategory.html");
        Category category = categoryService.getCategoryById(id);
        modelAndView.addObject("category", category);
        modelAndView.addObject("modelTypes", ModelType.values());
        modelAndView.addObject("routeTypes", RouteType.values());
//        Route route=category.getRoute();
//        modelAndView.addObject("routes",route!=null?route.getRouteType():null);
        return modelAndView;
    }

    /**
     * 新增栏目信息 xhl
     */
    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    @ResponseBody
    public ResultView saveCategory(String name,
                                   Integer model,
                                   Long siteId,
                                   Long parentId,
                                   Integer orderWeight,
                                   String rule,
                                   String template,
                                   String parentPath,
                                   Integer routeType) {
        ResultView result;
        try {
            Category category = new Category();
            Site site = siteRepository.findOne(siteId);
            if (!routeService.isPatterBySiteAndRule(site, rule)) {
                log.error("site-->" + site.hashCode());
                category.setName(name);
                category.setOrderWeight(orderWeight);
                Category categoryParent = categoryService.getCategoryById(parentId);
                category.setSite(site);
                category.setContentType(EnumUtils.valueOf(ContentType.class, model));
                category.setParent(categoryParent);
                category.setCreateTime(LocalDateTime.now());
                category.setUpdateTime(LocalDateTime.now());
//                log.error("site2-->"+site.hashCode());
//                if (categoryService.saveCategoryAndRoute(category, rule, template
//                        , EnumUtils.valueOf(RouteType.class, routeType))) {
//                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
//                } else {
//                    result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
//                }
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            } else {
                result = new ResultView(ResultOptionEnum.ROUTE_EXISTS.getCode()
                        , ResultOptionEnum.ROUTE_EXISTS.getValue(), null);
            }
        } catch (Exception ex) {
            log.error("saveCategory error", ex);
            result = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return result;
    }

    /**
     * 根据栏目ID来删除栏目 xhl
     */
    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteCategory(@RequestParam(name = "id", defaultValue = "0") Long id,
                                     HttpServletRequest request) {
        ResultView result;
        try {
            if (cookieUser.isSupper(request)) {
                Category category = categoryService.getCategoryById(id);
//                if(category.getOwnerId().equals(cookieUser.getOwnerId(request))) {//删除的时候验证是否是删除同一商户下面的栏目，增强安全性
                if (categoryService.deleteCategory(category)) {
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                } else {
                    result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
                }
//                }
//                else{
//                    result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
//                }
            } else {
                result = new ResultView(ResultOptionEnum.NO_LIMITS.getCode(), ResultOptionEnum.NO_LIMITS.getValue(), null);
            }
        } catch (Exception ex) {
            result = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), ResultOptionEnum.SERVERFAILE.getValue(), null);
        }
        return result;
    }

    /**
     * 新增栏目信息 xhl
     */
    @RequestMapping(value = "/modifyCategory", method = RequestMethod.POST)
    @ResponseBody
    public ResultView modifyCategory(@RequestParam(name = "id", required = true, defaultValue = "0") Long id,
                                     @RequestParam(name = "siteId", required = true) Long siteId,
                                     @RequestParam(name = "name", required = true) String name,
                                     @RequestParam(name = "modelId") Integer modelId,
                                     @RequestParam(name = "orderWeight") Integer orderWeight,
                                     @RequestParam(name = "rule") String rule,
                                     @RequestParam(name = "template") String template,
                                     @RequestParam(name = "noRule") String noRule,
                                     @RequestParam(name = "routeType") Integer routeType) {
        ResultView result = null;
        try {
            Category category = categoryService.getCategoryById(id);
            if (category != null) {
                Site site = category.getSite();
                if (!routeService.isPatterBySiteAndRuleIgnore(site, rule, noRule)) {
                    category.setSite(site);
                    category.setContentType(EnumUtils.valueOf(ContentType.class, modelId));
                    category.setName(name);
                    category.setOrderWeight(orderWeight);
                    category.setUpdateTime(LocalDateTime.now());
//                    categoryService.updateCategoryAndRoute(category, rule, template, noRule
//                            , EnumUtils.valueOf(RouteType.class, routeType));
                    result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
                }
            } else {
                result = new ResultView(ResultOptionEnum.NOFIND.getCode(), ResultOptionEnum.NOFIND.getValue(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = new ResultView(ResultOptionEnum.FAILE.getCode(), ResultOptionEnum.FAILE.getValue(), null);
        }
        return result;
    }

    @InitBinder
    public void bind(WebDataBinder binder) {
        if (binder.getObjectName().equals("parent")) {
            binder.addCustomFormatter(categoryFormatter);
        }
    }

    @Override
    protected Category preparePersist(Login login, Site site, Category data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        data.setSite(site);
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/category/index.html";
    }

    @Override
    protected void prepareSave(Login login, Category entity, Category data, Void extra, RedirectAttributes attributes)
            throws RedirectException {
        // 允许修改的东西其实并不多 就让他可以修改名字吧。
        entity.setName(data.getName());
    }

    @Override
    protected String openViewName() {
        // 没打算提供编辑页面
        return null;
    }
}
