package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>
 *     路由规则解析服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class RouteResolverService {
    @Autowired
    private RouteService routeService;

    /**
     * 根据url和站点信息获得路由规则
     *
     * @param site
     * @param url
     * @return
     * */
    public Route getRoute(Site site,String url) {
        Route routRuled=null;
        Set<Route> ruleds =routeService.getRout(site);
        for(Route s : ruleds) {
            if(PatternMatchUtil.match(url, s.getRule().toString()))
            {
                routRuled=s;
            }
        }
        return routRuled;
    }
}
