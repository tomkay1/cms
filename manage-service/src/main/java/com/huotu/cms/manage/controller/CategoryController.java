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
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.converter.ContentTypeConverter;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.ProductCategory;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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
    @Autowired
    private ContentTypeConverter contentTypeConverter;

    @Override
    protected String resourceName(Locale locale) {
        return "数据源";
    }

    @Override
    protected Specification<Category> prepareIndex(Login login, HttpServletRequest request, Site site, Model model
            , RedirectAttributes attributes) throws RedirectException {
        if (request.getParameter("fixedType") != null) {
            ContentType contentType = contentTypeConverter.convert(request.getParameter("fixedType"));
            if (contentType != null) {
                model.addAttribute("fixedType", contentType);
                return (root, query, cb) -> cb.and(cb.equal(root.get("site").as(Site.class), site)
                        , cb.equal(root.get("contentType").as(ContentType.class), contentType));
            }
        }
        return super.prepareIndex(login, request, site, model, attributes);
    }

    @Override
    protected Category preparePersist(Login login, Site site, Category data, Long extra, RedirectAttributes attributes)
            throws RedirectException {
        if (data.getContentType() == ContentType.Page)
            throw new RedirectException(rootUri(), "不可以创建页面数据源");
        if (data.getContentType() == ContentType.Product) {
            log.debug("use ProductCategory");
            ProductCategory productCategory = new ProductCategory();
            productCategory.setContentType(data.getContentType());
            productCategory.setName(data.getName());
            data = productCategory;
        }
        data.setSite(site);
        if (extra != null) {
            data.setParent(categoryService.get(extra));
            if (data.getParent().getContentType() != data.getContentType())
                throw new RedirectException(rootUri(), "不可以创建不同于父级内容类型的数据源");
        }
        categoryService.init(data);
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
        return "view/category/category.html";
    }
}
