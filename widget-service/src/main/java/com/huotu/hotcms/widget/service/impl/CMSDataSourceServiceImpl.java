/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CJ
 */
@Service
public class CMSDataSourceServiceImpl implements CMSDataSourceService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    public List<Gallery> findGallery() {
        Site site = CMSContext.RequestContext().getSite();
        return galleryRepository.findByCategory_site(site);
    }

    @Override
    public List<GalleryItem> findGalleryItem(Long galleryId) {
        return null;
    }

    @Override
    public List<Category> findLinkCategory() {
        return null;
    }

    @Override
    public List<Category> findParentArticleCategorys() {
        return null;
    }

    @Override
    public String findChildrenArticleCategory(Long parentId) {
        return null;
    }

    @Override
    public String findSitePage() {
        return null;
    }

    @Override
    public List<Link> findLink(Long categoryId) {
        return null;
    }
}
