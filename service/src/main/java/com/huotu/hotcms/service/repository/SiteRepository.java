package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
public interface SiteRepository  extends JpaRepository<Site, Long>,JpaSpecificationExecutor {
    Site findByCustomerIdAndName(Integer customId,String name);

    List<Site> findByHosts(Host host);

    Set<Site> findByCustomerId(int customerId);

    List<Site> findByCustomerIdAndDeletedOrderBySiteIdDesc(int customerId,Boolean deleted);

    Set<Site> findByCustomerIdAndDeleted(int customerId,Boolean deleted);

    Site findBySiteIdAndCustomerId(Long siteId,int customerId);

    Site findByTitle(String title);

    List<Site> findByCustomerIdAndDeletedAndPersonaliseOrderBySiteIdDesc(int customerId,Boolean deleted,Boolean Personalise);

}
