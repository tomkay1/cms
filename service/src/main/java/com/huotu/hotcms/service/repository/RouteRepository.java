package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by xhl on 2016/1/5.
 */
public interface RouteRepository extends JpaRepository<Route, Long>,JpaSpecificationExecutor {
    Set<Route> findBySite(Site site);

    Route findBySiteAndRouteType(Site site,RouteType routeType);
}
