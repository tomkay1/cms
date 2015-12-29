package com.huotu.hotcms.admin.interceptor;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.service.HostService;
import com.huotu.hotcms.service.RegionService;
import com.huotu.hotcms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/21.
 */
@Component
public class SiteResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private HostService hostService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private SiteService siteService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String language = getLanguage(request);
        if("".equals(language)) {
            return initSiteParameter(request);
        }
        String domain = request.getServerName();
        return getSite(domain,language);
    }

    public String getLanguage(HttpServletRequest request) throws Exception{
        String path = request.getServletPath();
        String language = "";
        if("/".equals(path)) {
            language = request.getLocale().getLanguage();
            if("".equals(language)) {
                language = "zh";
            }
        }else if(path.lastIndexOf("/") == 0) {
            String regionCode = path.substring(1);
            language = regionService.getRegion(regionCode).getLangCode();
        }
        if(language == null) {
            throw new Exception("页面不存在");
        }
        return language;
    }

    public Site getSite(String domain,String language) throws Exception {
        Site site = new Site();
        Host host = hostService.getHost(domain);
        if(host!=null) {
            Set<Site> siteList = host.getSites();
            for (Site s : siteList) {
                if (s.getRegion().getLangCode().equalsIgnoreCase(language)) {
                    site = s;
                    break;
                }
            }
        }
        return site;
    }

    private Site initSiteParameter(HttpServletRequest request) throws Exception {
        Site site = new Site();
        boolean initSuccess = false;
        if(!StringUtils.isEmpty(request.getParameter("siteId"))) {
            site = siteService.getSite(Long.parseLong(request.getParameter("siteId")));
            if(site == null) {
                throw new Exception("站点不存在");
            }
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("customerId"))) {
            site.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("name"))) {
            site.setName(request.getParameter("name"));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("title"))) {
            site.setTitle(request.getParameter("title"));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("keywords"))) {
            site.setKeywords(request.getParameter("keywords"));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("description"))) {
            site.setDescription(request.getParameter("description"));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("copyright"))) {
            site.setCopyright(request.getParameter("copyright"));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("custom"))) {
            site.setCustom("0".equals(request.getParameter("custom")) ? false : true);
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("customTemplateUrl"))) {
            site.setCustomTemplateUrl(request.getParameter("customTemplateUrl"));
            initSuccess = true;
        }
        if(!initSuccess) {
            throw new Exception("初始化参数失败");
        }
        return site;
    }
}
