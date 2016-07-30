/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CollapseArtcleCategory;
import com.huotu.hotcms.service.model.NavbarPageInfoModel;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
@Service("cmsDataSourceService")
public class CMSDataSourceServiceImpl implements CMSDataSourceService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;

    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Gallery> findGallery() {
        Site site = CMSContext.RequestContext().getSite();
        return galleryRepository.findByCategory_site(site);
    }

    @Override
    public List<GalleryItem> findGalleryItem(Long galleryId) {
        if (!categoryRepository.findOne(galleryId).getContentType().equals(ContentType.Gallery)) {
            return null;
        }
        return galleryItemRepository.findByGallery(galleryRepository.getOne(galleryId));
    }

    @Override
    public List<Category> findLinkCategory() {
        return categoryRepository.findBySiteAndContentType(CMSContext.RequestContext().getSite(), ContentType.Link);
    }

    @Override
    public List<Category> findParentArticleCategory() {
        return categoryRepository.findBySiteAndParent(CMSContext.RequestContext().getSite(), null);
    }

    @Override
    public String findChildrenArticleCategory(Long parentId) {
        if (!categoryRepository.findOne(parentId).getContentType().equals(ContentType.Article)) {
            return null;
        }

        List<Category> list = categoryRepository.findByParent_Id(parentId);
        List<CollapseArtcleCategory> collapseArtcleCategories = new ArrayList<>();
        for (Category category : list) {
            CollapseArtcleCategory collapseArtcleCategory = new CollapseArtcleCategory();
            collapseArtcleCategory.setText(category.getName());
            collapseArtcleCategory.setHref(category.getSerial());
            collapseArtcleCategory.setCategoryId(category.getId());
            collapseArtcleCategory.setParentId(category.getParent() != null ? category.getParent().getId() : 0);
            collapseArtcleCategories.add(collapseArtcleCategory);
        }
        List<CollapseArtcleCategory> rootTrees = new ArrayList<>();
        for (CollapseArtcleCategory collapseArtcleCategory : collapseArtcleCategories) {
            if (collapseArtcleCategory.getParentId() == 0) {
                rootTrees.add(collapseArtcleCategory);
            }
            for (CollapseArtcleCategory t : collapseArtcleCategories) {
                if (t.getParentId() == collapseArtcleCategory.getCategoryId()) {
                    collapseArtcleCategory.getNodes().add(t);
                }
            }
        }
        return JSONObject.toJSONString(rootTrees);
    }

    @Override
    public String findSitePage() {
        List<PageInfo> list = pageInfoRepository.findBySite(CMSContext.RequestContext().getSite());
        List<NavbarPageInfoModel> navbarPageInfoModels = new ArrayList<>();
        for (PageInfo pageInfo : list) {
            NavbarPageInfoModel navbarPageInfoModel = new NavbarPageInfoModel();
            navbarPageInfoModel.setText(pageInfo.getTitle());
            navbarPageInfoModel.setHref(pageInfo.getPagePath());
            navbarPageInfoModel.setPageId(pageInfo.getPageId());
            navbarPageInfoModel.setParentId(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        List<NavbarPageInfoModel> rootTrees = new ArrayList<>();
        for (NavbarPageInfoModel navbarPageInfoModel : navbarPageInfoModels) {
            if (navbarPageInfoModel.getParentId() == 0) {
                rootTrees.add(navbarPageInfoModel);
            }
            for (NavbarPageInfoModel t : navbarPageInfoModels) {
                if (t.getParentId() == navbarPageInfoModel.getPageId()) {
                    navbarPageInfoModel.getNodes().add(t);
                }
            }
        }
        return JSONObject.toJSONString(rootTrees);
    }

    @Override
    public List<Link> findLink(Long categoryId) {
        if (categoryRepository.findOne(categoryId).getContentType().equals(ContentType.Link))
            return linkRepository.findByCategory_id(categoryId);
        else
            return null;
    }
}
