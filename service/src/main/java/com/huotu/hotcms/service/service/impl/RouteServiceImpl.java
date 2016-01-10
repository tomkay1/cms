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

    @Override
    public Set<Route> getRouteByRuleAndSite(Site site, String rule) {
        return routeRepository.findBySiteAndRule(site, rule);
    }

    @Override
    public Boolean isExistsBySiteAndRule(Site site, String rule) {
       Set<Route> routeSet=routeRepository.findBySiteAndRule(site,rule);
       return (routeSet!=null&&routeSet.size()>0)?true:false;
    }

    @Override
    public Boolean isPatterBySiteAndRule(Site site, String rule) {
        Set<Route> routes =getRoute(site);
        for(Route s : routes) {
            if(s.getRule()!=null) {
                if(rule.matches(s.getRule())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean isPatterBySiteAndRuleIgnore(Site site, String rule, String noRule) {
        Set<Route> routes =getRoute(site);
        for(Route s : routes) {
            if(s.getRule()!=null) {
                if(!s.getRule().equals(noRule)) {
                    if (rule.matches(s.getRule())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean save(Route route) {
        Route route1= routeRepository.save(route);
        return  route1!=null;
    }
}
