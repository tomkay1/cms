package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.DataModel;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RouteRepository;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void delete(Route route) {
        routeRepository.delete(route);
    }

    @Override
    public PageData<Route> getPage(Site site,String description,Integer page,Integer pageSize) {
        PageData<Route> data = new PageData<Route>();
//        Specification<Route> specification = (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (!StringUtils.isEmpty(description)) {
//                predicates.add(cb.like(root.get("description").as(String.class), "%" + description + "%"));
//            }
//            if(siteId!=null){
//                predicates.add(cb.and(root.get("description").as(String.class), "%" + description + "%"));
//            }
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//        };
//        Page<Route> pageData = routeRepository.findAll(specification,new PageRequest(page - 1, pageSize));
//        data=data.ConvertPageData(pageData,new Route[pageData.getContent().size()]);
        return  data;
    }
}
