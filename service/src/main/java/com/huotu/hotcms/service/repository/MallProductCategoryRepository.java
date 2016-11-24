package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.MallProductCategory;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 商城数据源
 * Created by lhx on 2016/9/29.
 */
public interface MallProductCategoryRepository extends JpaRepository<MallProductCategory, Long>
        , JpaSpecificationExecutor<MallProductCategory> {
    List<MallProductCategory> findBySiteAndDeletedFalse(Site site);

    List<MallProductCategory> findBySiteAndParent_SerialAndDeletedFalse(Site site, String serial);

    MallProductCategory findBySerial(String serial);

}
