package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.service.RequestService;
import com.huotu.hotcms.web.service.RoutResolverService;
import com.huotu.hotcms.web.service.SiteResolveService;
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
public class RoutInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SiteResolveService siteResolveService;

    @Autowired
    private RoutResolverService routResolverService;

    @Autowired
    private RequestService requestService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String servletPath=request.getServletPath();
        Site site=siteResolveService.getCurrentSite(request);
        Route ruled=null;
        if(site!=null){
            ruled=routResolverService.getRout(site,servletPath);
            if(modelAndView==null){
                modelAndView=new ModelAndView();
            }
            if(ruled!=null){
                modelAndView.setViewName(site.getCustomTemplateUrl() + ruled.getTemplate());
                modelAndView.addObject("site",site);
                modelAndView.addObject("request",requestService.ConvertRequestModel(request));
            }
        }
    }
}
