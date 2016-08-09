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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.CollapseArtcleCategory;
import com.huotu.hotcms.service.model.GalleryItemModel;
import com.huotu.hotcms.service.model.LinkModel;
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
        return galleryRepository.findByCategory_Site(site);
    }

    @Override
    public List<GalleryItemModel> findGalleryItem(Long galleryId) {
        List<GalleryItem> galleryItems = galleryItemRepository.findByGallery(galleryRepository.getOne(galleryId));
        List<GalleryItemModel> galleryItemModels = new ArrayList<>();
        if (galleryItems != null && galleryItems.size() > 0) {
            for (GalleryItem item : galleryItems) {
                galleryItemModels.add(GalleryItem.getGalleryItemModel(item));
            }
        }
        return galleryItemModels;
    }

    @Override
    public List<Category> findLinkCategory() {
        return categoryRepository.findBySiteAndContentType(CMSContext.RequestContext().getSite(), ContentType.Link);
    }

    @Override
    public List<Category> findParentArticleCategory() {
        return categoryRepository.findBySiteAndContentTypeAndParent(CMSContext.RequestContext().getSite()
                , ContentType.Article, null);
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
            navbarPageInfoModel.setName(pageInfo.getTitle());
            navbarPageInfoModel.setId(pageInfo.getPageId());
            navbarPageInfoModel.setPagePath(pageInfo.getPagePath());
            navbarPageInfoModel.setPid(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(navbarPageInfoModels);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public List<LinkModel> findLink(Long categoryId) {
        List<LinkModel> linkModels = new ArrayList<>();
        if (categoryRepository.findOne(categoryId).getContentType().equals(ContentType.Link)) {
            List<Link> links = linkRepository.findByCategory_Id(categoryId);
            if (links != null && links.size() > 0) {
                for (Link link : links) {
                    linkModels.add(Link.getLinkModel(link));
                }
            }
        }
        return linkModels;
    }

    @Override
    public String findSiteNotParentPage() {
        List<PageInfo> list = pageInfoRepository.findBySiteAndParent(CMSContext.RequestContext().getSite(), null);
        List<NavbarPageInfoModel> navbarPageInfoModels = new ArrayList<>();
        for (PageInfo pageInfo : list) {
            NavbarPageInfoModel navbarPageInfoModel = new NavbarPageInfoModel();
            navbarPageInfoModel.setName(pageInfo.getTitle());
            navbarPageInfoModel.setId(pageInfo.getPageId());
            navbarPageInfoModel.setPagePath(pageInfo.getPagePath());
            navbarPageInfoModel.setPid(pageInfo.getParent() != null ? pageInfo.getParent().getPageId() : 0);
            navbarPageInfoModels.add(navbarPageInfoModel);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(navbarPageInfoModels);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
