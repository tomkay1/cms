package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface CustomPagesRepository extends JpaRepository<CustomPages, Long>,JpaSpecificationExecutor {
    List<CustomPages> findByHomeAndSite(Boolean home,Site site);

    @Query(value = "update CustomPages set home=false where site=?1")
    void updateCustomerPageHome(Site site);

    CustomPages findBySiteAndSerial(Site site,String serial);
}
