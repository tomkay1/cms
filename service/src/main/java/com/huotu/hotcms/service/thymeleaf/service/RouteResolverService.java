/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.RouteService;
import com.huotu.hotcms.service.thymeleaf.common.ConfigInfo;
import com.huotu.hotcms.service.util.PatternMatchUtil;
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
        if(PatternMatchUtil.match(url, PatternMatchUtil.routeUrlRegexp)) {
            url = PatternMatchUtil.getUrlString(url, PatternMatchUtil.routeUrlRegexp);
        }
        Set<Route> routes =routeService.getRoute(site);
        for(Route s : routes) {
            if(s.getRule()!=null) {
                if(s.getRule().contains("?")){
                    String ruleStr= s.getRule().substring(0,s.getRule().indexOf("?"));
                    if (url.matches(ruleStr)) {
                        return s;
                    }
                }else {
                    if (url.matches(s.getRule())) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据站点和路由模型获得路由规则
     *
     * @param site
     * @param routeType
     * @return
     * */
    public Route getRouteByRouteType(Site site,RouteType routeType){
        return routeService.getRouteByRouteType(site,routeType);
    }

    /**
     * 根据站点和路由类型获得错误页面路由模版
     *
     * @param site
     * @param routeType
     * @return
     * */
    public String getRouteTemplate(Site site,RouteType routeType){
        if(site!=null&&routeType!=null){
            String resourcePath = site.isCustom() ? site.getCustomTemplateUrl() :
                    ConfigInfo.getRootTemplate(site.getOwner().getId());
            if(routeType.getCode().equals(RouteType.NOT_FOUND.getCode())){
                Route route=getRouteByRouteType(site, routeType);
                if(route!=null){
                    return resourcePath + route.getTemplate();//404路由模版
                }
            }
            if(routeType.getCode().equals(RouteType.SERVER_ERROR.getCode())){
                Route route=getRouteByRouteType(site, routeType);
                if(route!=null){
                    return resourcePath + route.getTemplate();//服务器错误路由模版
                }
            }
        }
        return "";//我们系统默认404页面,后期添加这个页面
    }
}
