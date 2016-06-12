package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/1/10.
 */

import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.model.thymeleaf.foreach.GalleryForeachParam;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;

public interface GalleryListService {


    /**
     * 获取gallery的分页
     *
     * @param customerId
     * @param galleryId
     * @param page
     * @param pageSize
     * @return
     * @throws URISyntaxException
     */
    Page<GalleryList> getPage(Integer customerId, Long galleryId, int page, int pageSize) throws URISyntaxException;

    Boolean saveGalleryList(GalleryList galleryList);

    GalleryList findGalleryListById(Long id);

    Page<GalleryList> getGalleryList(GalleryForeachParam foreachParam) throws Exception;
}
