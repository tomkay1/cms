package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/10.
 */
public interface GalleryListRepository extends JpaRepository<GalleryList, Long>,JpaSpecificationExecutor {
    List<GalleryList> findByGallery(Gallery gallery);
}
