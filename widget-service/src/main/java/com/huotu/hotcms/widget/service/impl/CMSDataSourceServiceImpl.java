/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.GalleryItemRepository;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.repository.LinkRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CJ
 */
@Service("cmsDataSourceService")
public class CMSDataSourceServiceImpl implements CMSDataSourceService {

    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Gallery> findGallery() {
        Site site = CMSContext.RequestContext().getSite();
        return galleryRepository.findByCategory_site(site);
    }

    @Override
    public List<GalleryItem> findGalleryItem(Long galleryId) {
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
        throw new NoSuchMethodError("不会写。。");
    }

    @Override
    public String findSitePage() {
        throw new NoSuchMethodError("不会写。。");
    }

    @Override
    public List<Link> findLink(Long categoryId) {
        return linkRepository.findByCategory_id(categoryId);
    }
}
