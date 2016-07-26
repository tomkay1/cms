/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.model.thymeleaf.foreach.GalleryForeachParam;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;

public interface GalleryListService {

    /**
     * 获取gallery的分页
     *
     * @param ownerId
     * @param galleryId
     * @param page
     * @param pageSize
     * @return
     * @throws URISyntaxException
     */
    Page<GalleryItem> getPage(long ownerId, Long galleryId, int page, int pageSize) throws URISyntaxException;

    Boolean saveGalleryList(GalleryItem galleryItem);

    GalleryItem findGalleryListById(Long id);

    Page<GalleryItem> getGalleryList(GalleryForeachParam foreachParam) throws Exception;
}
