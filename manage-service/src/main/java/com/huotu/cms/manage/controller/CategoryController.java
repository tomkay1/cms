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
public class CategoryController extends SiteManageController<Category, Long, Long, Void> {
    private static final Log log = LogFactory.getLog(CategoryController.class);


    @Autowired
    CategoryService categoryService;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    SiteService siteService;
    @Autowired
    RouteService routeService;
    @Override
    protected Category preparePersist(Login login, Site site, Category data, Long extra, RedirectAttributes attributes)
            throws RedirectException {
        data.setSite(site);
        if (extra != null) {
            data.setParent(categoryService.get(extra));
        }
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/category/index.html";
    }

    @Override
    protected void prepareUpdate(Login login, Category entity, Category data, Void extra, RedirectAttributes attributes)
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
