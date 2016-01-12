package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/1/10.
 */

import com.huotu.hotcms.service.entity.Gallery;

public interface  GalleryService {
    Boolean saveGallery(Gallery gallery);
    Gallery findById(Long id);
}
