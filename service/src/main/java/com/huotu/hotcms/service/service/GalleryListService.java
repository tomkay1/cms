package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/1/10.
 */

import com.huotu.hotcms.service.entity.GalleryList;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;

public interface GalleryListService {



    Page<GalleryList> getPage(Integer customerId, Long galleryId, int page, int pageSize) throws URISyntaxException;

    Boolean saveGalleryList(GalleryList galleryList);

    GalleryList findGalleryListById(Long id);

    Page<GalleryList> getGalleryList(PageableForeachParam foreachParam) throws Exception;
}
