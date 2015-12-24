package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.common.LanguageType;
import com.huotu.hotcms.entity.Region;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/21.
 */
@Component
public class SiteResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private RegionService regionService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Site site = new Site();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String area = request.getLocale().getCountry();
        String serverName = request.getServerName();
        Region region = regionService.getRegion(area);
        site.setRegion(region);
        site.setTitle("test");
        return site; // select by SiteDomain
    }
}
