package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.common.ConfigInfo;
import com.huotu.hotcms.web.service.ArticleResolveService;
import com.huotu.hotcms.web.service.RequestService;
import com.huotu.hotcms.web.service.RouteResolverService;
import com.huotu.hotcms.web.service.SiteResolveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

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
    private static final Log log = LogFactory.getLog(RouteInterceptor.class);

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
        if(site!=null){
            Route route=routeResolverService.getRoute(site,servletPath);
            if(modelAndView==null){
                modelAndView=new ModelAndView();
            }
            if(route==null){
                modelAndView.setViewName(routeResolverService.getRouteTemplate(site,RouteType.NOT_FOUND));//请求路径错误给出404容错页面
            }
            initModelAndView(modelAndView, site, route, request,response);
        }else {
            modelAndView.setViewName(routeResolverService.getRouteTemplate(site,RouteType.SERVER_ERROR));//解析站点错误给出容错页面
        }
    }

    private ModelAndView initModelAndView(ModelAndView modelAndView, Site site, Route route, HttpServletRequest request,HttpServletResponse response){
        try {
            String resourcePath = site.isCustom() ? site.getCustomTemplateUrl() : ConfigInfo.getRootTemplate(site.getCustomerId());
            if(route!=null) {
                if (route.getRouteType() != null) {
                    if (route.getRouteType().getCode().equals(RouteType.ARTICLE_CONTENT.getCode())) {
                        Article article = articleResolveService.getArticleBySiteAndRequest(site, request);
                        if (article != null) {
                            modelAndView.addObject("article", article);
                            modelAndView.setViewName(resourcePath + route.getTemplate());
                        } else {
                            modelAndView.setViewName(routeResolverService.getRouteTemplate(site, RouteType.NOT_FOUND));
                        }
                    } else {
                        modelAndView.setViewName(resourcePath + route.getTemplate());
                    }
                } else {
                    modelAndView.setViewName(resourcePath + route.getTemplate());
                }
                modelAndView.addObject("route", route);
                modelAndView.addObject("site", site);
                modelAndView.addObject("resourcePath", resourcePath);
                modelAndView.addObject("request", requestService.ConvertRequestModel(request));
            }else{
                modelAndView.setViewName(resourcePath +request.getServletPath());
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return modelAndView;
    }
}
