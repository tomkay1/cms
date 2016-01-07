package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.service.ArticleResolveService;
import com.huotu.hotcms.web.service.RequestService;
import com.huotu.hotcms.web.service.RouteResolverService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *     路由规则拦截器
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class RouteInterceptor  extends HandlerInterceptorAdapter {

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private RouteResolverService routeResolverService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ArticleResolveService articleResolveService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String servletPath=request.getServletPath();
        Site site=siteResolveService.getCurrentSite(request);
        Route route=null;
        if(site!=null){
            route=routeResolverService.getRoute(site,servletPath);
            if(modelAndView==null){
                modelAndView=new ModelAndView();
            }
            if(route!=null){
               modelAndView=getModelAndView(modelAndView,site,route,request);
            }
        }
    }

    private ModelAndView getModelAndView(ModelAndView modelAndView,Site site,Route route,HttpServletRequest request){
        if(route.getRouteType()!=null) {
            if (route.getRouteType().getCode() == RouteType.ARTICLEDETILE.getCode()) {
                Article article = articleResolveService.getArticleBySiteAndRequest(site, request);
                if (article != null) {
                    modelAndView.addObject("article", article);
                    modelAndView.setViewName(site.getCustomTemplateUrl() + route.getTemplate());
                } else {
                    modelAndView.setViewName(site.getCustomTemplateUrl() + route.getTemplate());//设置404页面模版
                }
            } else {
                modelAndView.setViewName(site.getCustomTemplateUrl() + route.getTemplate());
            }
        }else{
            modelAndView.setViewName(site.getCustomTemplateUrl() + route.getTemplate());
        }
        modelAndView.addObject("site",site);
        modelAndView.addObject("request", requestService.ConvertRequestModel(request));
        return modelAndView;
    }
}
