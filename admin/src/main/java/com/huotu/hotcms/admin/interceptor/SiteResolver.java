package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/12/21.
 */
@Component
public class SiteResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private HostService hostService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String customerId = request.getParameter("customerId");
        Site site = new Site();
        /*if(customerId==null) {
            String domain = request.getServerName();
            site = hostService.getSite(domain).getSite();
            String path = request.getContextPath();
            String path2 = request.getServletPath();
            String area = request.getLocale().getCountry();
            Region region = RegionService.getRegion(area);
            site.setRegion(region);
        }else {

        }*/
//        request.getContextPath()

        return site; // select by SiteDomain
    }
}
