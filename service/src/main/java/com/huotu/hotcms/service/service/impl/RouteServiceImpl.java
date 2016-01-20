package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.*;
import com.huotu.hotcms.service.repository.RouteRepository;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.StringUtil;
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

    @Autowired
    private RegionService regionService;

    @Override
    public Set<Route> getRoute(Site site) {
        return routeRepository.findBySite(site);
    }

    @Override
    public Route getRouteByRouteType(Site site, RouteType routeType){
        return routeRepository.findBySiteAndRouteType(site,routeType);
    }

    @Override
    public Route getRouteByRuleAndSite(Site site, String rule) {
        return routeRepository.findBySiteAndRule(site, rule);
    }

    @Override
    public Boolean isExistsBySiteAndRule(Site site, String rule) {
       Route routeSet=routeRepository.findBySiteAndRule(site,rule);
       return routeSet!=null?true:false;
    }

    @Override
    public Boolean isPatterBySiteAndRule(Site site, String rule) throws Exception {
        try {
            if (!StringUtils.isEmpty(rule)) {
                if (isExistsRegion(rule)) {
                    return true;
                } else {
                    Set<Route> routes = getRoute(site);
                    if(routes!=null) {
                        for (Route s : routes) {
                            if(s!=null) {
                                if (s.getRule() != null) {
                                    String dataRule = s.getRule();
                                    if (rule.equals(dataRule) || rule.matches(dataRule)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            throw new Exception("isPatterBySiteAndRule exception "+ ex.toString());
        }
        return false;
    }

    @Override
    public Boolean isPatterBySiteAndRuleIgnore(Site site, String rule, String noRule) {
        if(!StringUtils.isEmpty(rule)) {
            if(isExistsRegion(rule)) {
                return true;
            }else {
                Set<Route> routes = getRoute(site);
                for (Route s : routes) {
                    String dataRule = s.getRule();
                    if (dataRule != null) {
                        if (!dataRule.equals(noRule)) {
                            if (rule.equals(dataRule) || rule.matches(s.getRule())) {
                                return true;
                            }
                        }
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
        Specification<Route> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(description)) {
                predicates.add(cb.like(root.get("description").as(String.class), "%" + description + "%"));
            }
            if(site!=null){
                predicates.add(cb.equal(root.get("site").as(Site.class), site));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Route> pageData = routeRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        data=data.ConvertPageData(pageData,new Route[pageData.getContent().size()]);
        for(Route route:pageData){
            route.setSite(null);
        }
        return  data;
    }

    @Override
    public Route getRoute(long id) {
       return routeRepository.findOne(id);
    }

    @Override
    public Boolean deleteRoute(Route route) {
        return false;
//        try{
//            Category category=
//        }catch (Exception ex){
//
//        }
    }

    /**
     * 根据路由规则来判断是否存在地区语言(用于过滤路由规则以及路由内置关键字web)
     * @param rule 路由规则
     * */
    @Override
    public Boolean isExistsRegion(String rule) {
        String langParam= StringUtil.getFirstParam(rule,"web");
        if(!StringUtils.isEmpty(langParam)) {
            if (langParam.contains("-")) {
                String[] list = langParam.split("-");
                if (list != null && list.length >= 2) {
                    String langCode = list[0];
                    String regionCode = list[1];
                    Region region = regionService.getRegionByLangCodeAndRegionCode(langCode, regionCode);
                    return region != null;
                }
            } else {
                if (langParam.equalsIgnoreCase("web")) {
                    return true;
                } else {
                    Region region = regionService.getRegionByCode(langParam);
                    return region != null;
                }
            }
        }
        return false;
    }
}
