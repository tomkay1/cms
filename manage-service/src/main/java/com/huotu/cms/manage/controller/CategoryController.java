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
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.repository.MallProductCategoryRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private MallService mallService;
    @Autowired
    private GalleryService galleryService;
    @Autowired
    private ContentTypeConverter contentTypeConverter;
    @Autowired
    private MallProductCategoryRepository mallProductCategoryRepository;

    @Override
    protected String resourceName(Locale locale) {
        return "数据源";
    }

    @Override
    protected void prepareOpen(Login login, HttpServletRequest request, Category data, Model model
            , RedirectAttributes attributes) throws RedirectException {
        mallData(data.getSite(), model);
        super.prepareOpen(login, request, data, model, attributes);
    }

    private void mallData(Site site, Model model) throws RedirectException {
        try {
            if (site.getOwner().getCustomerId() != null) {
                model.addAttribute("categoryList", mallService.listCategories(site.getOwner().getCustomerId()));
                model.addAttribute("brandList", mallService.listBrands(site.getOwner().getCustomerId()));
                model.addAttribute("galleries", galleryService.listGalleries(site));
                model.addAttribute("mallProducts", mallProductCategoryRepository.findBySite(site));
                model.addAttribute("links", categoryService.getCategoriesForContentType(site, ContentType.Link));
            }
        } catch (IOException ex) {
            throw new RedirectException(rootUri(), ex);
        }

    }

    @Override
    protected Specification<Category> prepareIndex(Login login, HttpServletRequest request, Site site, Model model
            , RedirectAttributes attributes) throws RedirectException {
        mallData(site, model);
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
    protected Category preparePersist(Login login, Site site, Category data, Long extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {
        if (data.getContentType() == ContentType.Page)
            throw new RedirectException(rootUri(), "不可以创建页面数据源");
        if (data.getContentType() == ContentType.Product) {
            log.debug("use ProductCategory");
            ProductCategory productCategory = new ProductCategory();
            productCategory.setContentType(data.getContentType());
            productCategory.setName(data.getName());
            data = productCategory;
        } else if (data.getContentType() == ContentType.MallProduct) {
            MallProductCategory mallProductCategory = new MallProductCategory();
            String goodTitle = request.getParameter("goodTitle");
            int salesCount = NumberUtils.parseNumber(request.getParameter("salesCount"), Integer.class);
            double minPrice = NumberUtils.parseNumber(request.getParameter("minPrice"), Double.class);
            double maxPrice = NumberUtils.parseNumber(request.getParameter("maxPrice"), Double.class);
            String mallCategoryId = request.getParameter("mallCategoryId");
            String mallBrands = request.getParameter("mallBrandId");
            String galleryId = request.getParameter("gallery");
            List<Long> categoryList;
            if (mallCategoryId != null && !mallCategoryId.equals("")) {
                categoryList = new ArrayList<>();
                String[] categorystr = mallCategoryId.split(",");
                for (String idstr : categorystr) {
                    Long id = NumberUtils.parseNumber(idstr, Long.class);
                    categoryList.add(id);
                }
                mallProductCategory.setMallCategoryId(categoryList);
            }
            List<Long> brandList;
            if (mallBrands != null && !mallBrands.equals("")) {
                brandList = new ArrayList<>();
                String[] brandstr = mallBrands.split(",");
                for (String idstr : brandstr) {
                    Long id = NumberUtils.parseNumber(idstr, Long.class);
                    brandList.add(id);
                }
                mallProductCategory.setMallBrandId(brandList);
            }
            if (galleryId != null && !galleryId.equals("")) {
                mallProductCategory.setGallery(galleryService.findById(NumberUtils.parseNumber(galleryId, Long.class)));
            }
            mallProductCategory.setParent(extra == null ? null : categoryService.get(extra));
            mallProductCategory.setContentType(data.getContentType());
            mallProductCategory.setName(data.getName());
            mallProductCategory.setSite(site);
            mallProductCategory.setGoodTitle(goodTitle);
            mallProductCategory.setMaxPrice(maxPrice);
            mallProductCategory.setMinPrice(minPrice);
            mallProductCategory.setSalesCount(salesCount);
            data = mallProductCategory;
        } else if (data.getContentType() == ContentType.MallClass) {
            String categories = request.getParameter("categories");
            String recommendCategory = request.getParameter("recommendCategory");
            MallClassCategory mallClassCategory = new MallClassCategory();
            mallClassCategory.setContentType(data.getContentType());
            mallClassCategory.setParent(data.getParent());
            mallClassCategory.setName(data.getName());
            mallClassCategory.setSerial(data.getSerial());
            mallClassCategory.setSite(data.getSite());
            List<MallProductCategory> list;
            if (categories != null && !categories.equals("")) {
                list = new ArrayList<>();
                String[] categorystr = categories.split(",");
                for (String id : categorystr) {
                    MallProductCategory productCategory = mallProductCategoryRepository.findOne(NumberUtils.parseNumber(id, Long.class));
                    list.add(productCategory);
                }
                mallClassCategory.setCategories(list);
            }
            mallClassCategory.setRecommendCategory(recommendCategory == null || recommendCategory.equals("") ? null
                    : categoryService.get(NumberUtils.parseNumber(recommendCategory, Long.class)));
            data = mallClassCategory;
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
    protected void prepareUpdate(Login login, Category entity, Category data, Void extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException {
        // 允许修改的东西其实并不多 就让他可以修改名字吧。
        entity.setName(data.getName());
        if (entity.getContentType() == ContentType.MallProduct) {
            MallProductCategory mallProductCategory = (MallProductCategory) entity;
            String goodTitle = request.getParameter("goodTitle");
            int salesCount = NumberUtils.parseNumber(request.getParameter("salesCount"), Integer.class);
            double minPrice = NumberUtils.parseNumber(request.getParameter("minPrice"), Double.class);
            double maxPrice = NumberUtils.parseNumber(request.getParameter("maxPrice"), Double.class);
            String mallCategoryId = request.getParameter("mallCategoryId");
            String mallBrands = request.getParameter("mallBrandId");
            String galleryId = request.getParameter("gallery");
            List<Long> categoryList;
            if (mallCategoryId != null && !mallCategoryId.equals("")) {
                categoryList = new ArrayList<>();
                String[] categorystr = mallCategoryId.split(",");
                for (String idstr : categorystr) {
                    Long id = NumberUtils.parseNumber(idstr, Long.class);
                    categoryList.add(id);
                }
                mallProductCategory.setMallCategoryId(categoryList);
            }
            List<Long> brandList;
            if (mallBrands != null && !mallBrands.equals("")) {
                brandList = new ArrayList<>();
                String[] brandstr = mallBrands.split(",");
                for (String idstr : brandstr) {
                    Long id = NumberUtils.parseNumber(idstr, Long.class);
                    brandList.add(id);
                }
                mallProductCategory.setMallBrandId(brandList);
            }
            if (galleryId != null && !galleryId.equals("")) {
                mallProductCategory.setGallery(galleryService.findById(NumberUtils.parseNumber(galleryId, Long.class)));
            }
            mallProductCategory.setGoodTitle(goodTitle);
            mallProductCategory.setMaxPrice(maxPrice);
            mallProductCategory.setMinPrice(minPrice);
            mallProductCategory.setSalesCount(salesCount);
        } else if (entity.getContentType() == ContentType.MallClass) {
            MallClassCategory mallClassCategory = (MallClassCategory) entity;
            String categories = request.getParameter("categories");
            String recommendCategory = request.getParameter("recommendCategory");
            List<MallProductCategory> list;
            if (categories != null && !categories.equals("")) {
                list = new ArrayList<>();
                String[] categorystr = categories.split(",");
                for (String id : categorystr) {
                    MallProductCategory productCategory = mallProductCategoryRepository.findOne(NumberUtils.parseNumber(id, Long.class));
                    list.add(productCategory);
                }
                mallClassCategory.setCategories(list);
            }
            mallClassCategory.setRecommendCategory(recommendCategory == null || recommendCategory.equals("") ? null
                    : categoryService.get(NumberUtils.parseNumber(recommendCategory, Long.class)));

        }
    }

    @Override
    protected String openViewName() {
        // 没打算提供编辑页面
        return "view/category/category.html";
    }
}
