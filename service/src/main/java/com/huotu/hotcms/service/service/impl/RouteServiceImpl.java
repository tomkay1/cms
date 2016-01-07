package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RouteRepository;
import com.huotu.hotcms.service.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 *     路由服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Service
public class RouteServiceImpl  implements RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Override
    public Set<Route> getRoute(Site site) {
        return routeRepository.findBySite(site);
    }

    @Override
    public Route getRouteByRouteType(Site site, RouteType routeType){
        return routeRepository.findBySiteAndRouteType(site,routeType);
    }
}
