package com.huotu.hotcms.service.service;

/**
 * Created by chendeyu on 2016/1/10.
 */

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.model.thymeleaf.foreach.PageableForeachParam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface  GalleryService {
    Boolean saveGallery(Gallery gallery);
    Gallery findById(Long id);

    List<Gallery> getSpecifyGallerys(String[] specifyIds);

    Page<Gallery> getGalleryList(PageableForeachParam oreachParam) throws Exception;

}
