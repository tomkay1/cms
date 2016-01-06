package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.RouteRule;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by xhl on 2016/1/5.
 */
public interface RouteRuleRepository extends JpaRepository<RouteRule, Long>,JpaSpecificationExecutor {
    Set<RouteRule> findBySite(Site site);
}
