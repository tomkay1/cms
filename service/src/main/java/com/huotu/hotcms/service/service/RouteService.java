/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PageData;

import java.util.Set;

/**
 * <p>
 *     路由服务
 * </p>
 * @author xhl
 *
 * @since 1.0
 */
public interface RouteService {
    Set<Route> getRoute(Site site);

    Route getRouteByRuleAndSite(Site site,String rule);

    Boolean isExistsBySiteAndRule(Site site,String rule);

    Boolean isPatterBySiteAndRule(Site site,String rule) throws Exception;

    Boolean isPatterBySiteAndRuleIgnore(Site site,String rule,String noRule);

    Boolean save(Route route);

    void delete(Route route);

    PageData<Route> getPage(Site site,String description,Integer page,Integer pageSize);

    Route getRoute(long id);

    Boolean deleteRoute(Route route);

    Boolean isExistsRegion(String rule);
}
