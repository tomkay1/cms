/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.thymeleaf.service.ArticleResolveService;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.thymeleaf.service.RouteResolverService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>
 * 路由器拦截器
 * </p>
 *
 * @author xhl
 * @since 1.0
 */
@Component
public class RouteInterceptor extends HandlerInterceptorAdapter {

    private static final Log log = LogFactory.getLog(RouteInterceptor.class);

    private static final String manage = "/manage/";

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private RouteResolverService routeResolverService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ArticleResolveService articleResolveService;


//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return super.preHandle(request, response, handler);
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if(!request.getServletPath().contains("/manage")){
//            Site site = siteResolveService.getCurrentSite(request);
//            if (!site.isPersonalise()) {
//                String servletPath = PatternMatchUtil.getServletPath(site, request);
//                Route route = routeResolverService.getRoute(site, servletPath);
//                if (modelAndView == null) {
//                    modelAndView = new ModelAndView();
//                }
//                if (route == null) {
//                    if(!servletPath.contains(manage)){
//                        modelAndView.setViewName(routeResolverService.getRouteTemplate(site, RouteType.NOT_FOUND));
//                    }
//                }
//                initModelAndView(modelAndView, site, route, request, response);
//            } else {
//                modelAndView.addObject("site", site);
//                modelAndView.addObject("request", requestService.ConvertRequestModel(request, site));
//            }
//        }
//    }
//
//    /**
//     * 初始化ModelAndView 信息对象,并根据当前请求的环境获得相关数据信息,这样以达到CMS内置对象的扩展标签
//     *
//     * @param modelAndView
//     * @param site         站点信息对象
//     * @param route        当前请求对应的路由信息
//     * @param request
//     * @param response
//     * @return
//     */
//    private ModelAndView initModelAndView(ModelAndView modelAndView, Site site, Route route, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String resourcePath = site.isCustom() ? site.getCustomTemplateUrl() : ConfigInfo.getRootTemplate(site.getOwner().getId());
//            if (route != null) {
//                if (route.getRouteType() != null) {
//                    if (route.getRouteType().getCode().equals(RouteType.ARTICLE_CONTENT.getCode())) {
//                        Article article = articleResolveService.getArticleBySiteAndRequest(site, request);
//                        if (article != null) {
//                            if (article.getCategory().getSite().getSiteId().equals(site.getSiteId())) {
//                                modelAndView.addObject("article", article);
//                                modelAndView.setViewName(resourcePath + route.getTemplate());
//                            } else {//不是该商户下面的文章则给出404页面
//                                modelAndView.setViewName(routeResolverService.getRouteTemplate(site, RouteType.NOT_FOUND));
//                            }
//                        } else {
//                            modelAndView.setViewName(resourcePath + route.getTemplate());
//                        }
//                    } else {
//                        modelAndView.setViewName(resourcePath + route.getTemplate());
//                    }
//                } else {
//                    modelAndView.setViewName(resourcePath + route.getTemplate());
//                }
//                modelAndView.addObject("route", route);
//                modelAndView.addObject("site", site);
//                modelAndView.addObject("resourcePath", resourcePath);
//                modelAndView.addObject("request", requestService.ConvertRequestModel(request, site));
//            } else {
//                if(!request.getServletPath().contains(manage)){
//                    modelAndView.setViewName(resourcePath + request.getServletPath());
//                    modelAndView.addObject("request", requestService.ConvertRequestModel(request, site));
//                }
//            }
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//        return modelAndView;
//    }
}
