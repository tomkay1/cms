package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.repository.GalleryRepository;
import com.huotu.hotcms.service.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chendeyu on 2016/1/10.
 */
@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    GalleryRepository galleryRepository;
    @Override
    public Boolean saveGallery(Gallery gallery) {
        galleryRepository.save(gallery);
        return true;
    }

    @Override
    public Gallery findById(Long id) {
        Gallery gallery =  galleryRepository.findOne(id);
        return gallery;
    }
}
