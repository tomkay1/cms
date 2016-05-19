package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.SiteType;
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


    List<Site> findByHosts(Host host);

    /**
     * 查询模板库
     * @param customerId  商户ID *此处为系统用户
     * @return 模板库list
     */
    List<Site> findByIsTemplateSiteFalseAndCustomerId(long customerId);

    /**
     * 查询模板库，如果系统默认只有一个系统用户
     * @return 模板库list
     */
    List<Site> findByIsTemplateSiteFalse();


    /**
     * 根据商户查询所有站点
     */
    List<Site> findByCustomerIdAndDeletedOrderBySiteIdDesc(int customerId,Boolean deleted);

    Set<Site> findByCustomerIdAndDeleted(int customerId,Boolean deleted);

    /**
     * 根据商户,和站点查询所有站点
     */
    Site findBySiteIdAndCustomerId(Long siteId,int customerId);

    /**
     * 根据商户和个性化查询站点
     */
    List<Site> findByCustomerIdAndDeletedAndPersonaliseOrderBySiteIdDesc(int customerId,Boolean deleted,Boolean Personalise);

    /**
     * 根据商户和个性化,以及站点类型(Pc官网或者pc商城)查询站点
     */
    List<Site> findByCustomerIdAndDeletedAndPersonaliseAndSiteTypeOrderBySiteIdDesc(int customerId,Boolean deleted,Boolean Personalise,SiteType siteType);
}
