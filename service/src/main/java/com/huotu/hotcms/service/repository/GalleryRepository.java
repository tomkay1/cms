package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chendeyu on 2016/1/10.
 */
public interface GalleryRepository extends JpaRepository<Gallery, Long>,JpaSpecificationExecutor {

    List<Gallery> findByCategory(Category category);
}
