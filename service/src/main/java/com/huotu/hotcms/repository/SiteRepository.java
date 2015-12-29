package com.huotu.hotcms.repository;

import com.huotu.hotcms.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
public interface SiteRepository  extends JpaRepository<Site, Long>,JpaSpecificationExecutor {
    Site findByCustomerIdAndName(Integer customId,String name);

    Set<Site> findByCustomerId(int customerId);

    Site findBySiteIdAndCustomerId(Long siteId,int customerId);
}
