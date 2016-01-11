package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.DataModel;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Set;

/**
 * <p>
 *     路由服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
public interface RouteService {
    Set<Route> getRoute(Site site);

    Route getRouteByRouteType(Site site,RouteType routeType);

    Route getRouteByRuleAndSite(Site site,String rule);

    Boolean isExistsBySiteAndRule(Site site,String rule);

    Boolean isPatterBySiteAndRule(Site site,String rule);

    Boolean isPatterBySiteAndRuleIgnore(Site site,String rule,String noRule);

    Boolean save(Route route);

    void delete(Route route);

    PageData<Route> getPage(Site site,String description,Integer page,Integer pageSize);
}
