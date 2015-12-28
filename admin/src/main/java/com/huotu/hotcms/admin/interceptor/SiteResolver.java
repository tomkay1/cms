package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.SiteRepository;
import com.huotu.hotcms.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/21.
 */
@Component
public class SiteResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private HostService hostService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String regionCode = getRegionCode(request);
        String domain = request.getServerName();
        Site site = getSite(domain,regionCode);
        return site;
    }

    public String getRegionCode(HttpServletRequest request) {
        String path = request.getServletPath();
        String regionCode = "";
        if("/".equals(path)) {
            regionCode = request.getLocale().getCountry();
        }else if(path.lastIndexOf("/")==0) {
            regionCode = path.substring(1);
        }
        return regionCode;
    }

    public Site getSite(String domain,String regionCode) {
        Site site = new Site();
        Host host = hostService.getHost(domain);
        if(host!=null) {
            Set<Site> siteList = host.getSites();
            for (Site s : siteList) {
                if (s.getRegion().getRegionCode().toUpperCase().equals(regionCode.toUpperCase())) {
                    site = s;
                    break;
                }
            }
        }
        return site;
    }
}
