package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.common.LanguageType;
import com.huotu.hotcms.entity.Site;
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
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Site site = new Site();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Locale locale = request.getLocale();
        String lang = request.getHeader("Accept-Language");
        String submittedSiteId = request.getParameter("siteId");
        if(locale!=null) {

        }
        if (submittedSiteId!=null){
            return null;// select by HttpServletRequest
        }
        //

//        String host = request.getServerName();

        site.setTitle("test");
        return site; // select by SiteDomain
    }
}
