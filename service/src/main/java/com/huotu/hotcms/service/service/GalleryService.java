package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/1/10.
 */

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import org.springframework.data.domain.Page;

import java.net.URISyntaxException;

public interface  GalleryService {
    Boolean saveGallery(Gallery gallery);
    Gallery findById(Long id);

    Page<GalleryList> getPage(Integer customerId,Long galleryId, int page, int pageSize) throws URISyntaxException;

    Boolean saveGalleryList(GalleryList galleryList);

    GalleryList findGalleryListById(Long id);
}
