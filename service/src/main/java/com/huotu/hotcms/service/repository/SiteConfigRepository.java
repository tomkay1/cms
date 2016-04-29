package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.SiteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/4/29.
 */
public interface SiteConfigRepository  extends JpaRepository<SiteConfig, Long>,JpaSpecificationExecutor {
    SiteConfig findBySite(Site site);
}
