/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.service.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by cwb on 2015/12/21.
 */
@Component
public class SiteResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private HostService hostService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteRepository siteRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Site.class;
    }

    /**
     * 根据域名及语言偏好获得站点信息
     *
     * 请求路径
     *  场景A： www.mi.com
     *  场景B： www.mi.com/cn
     * 获取语言偏好
     *  场景A：获取浏览器语言偏好，如没有偏好，则设置为中文
     *  场景B：从数据库获得,没有则抛异常
     * 根据域名查找对应站点列表
     * 匹配站点：
     *  场景A：并且没有符合的站点，自动路由到中文站
     *  场景B：如果是指定区域,并且没有符合的站点，则抛异常
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String path = request.getServletPath();
        if(isRootPath(path)) {
            return getHomeSite(request);
        }else if(isSubSitePath(path)) {
            return getSubSite(request);
        }
        return null;
    }

    private Site getSubSite(HttpServletRequest request) throws Exception{
        Site site = null;
        String regionCode = request.getServletPath().substring(1);
        Region region = regionService.getRegion(regionCode);
        if(region == null) {
            throw new Exception("请求错误");
        }
        String language = region.getLangCode();
        String domain = request.getServerName();
        Set<Site> sites = getSitesThroughDomain(domain);
        for(Site s : sites) {
            if(s.getRegion().getLangCode().equalsIgnoreCase(language)) {
                site = s;
                break;
            }
        }
        if(site == null) {
            throw new Exception("页面不存在");
        }
        return site;
    }

    private Site getHomeSite(HttpServletRequest request) throws Exception{
        Site site = null;
        Site chSite = null;
        String domain = request.getServerName();
        Set<Site> sites = getSitesThroughDomain(domain);
        String language = request.getLocale().getLanguage();
        if(StringUtils.isEmpty(language)) {
            language = "zh";
        }
        for(Site s : sites) {
            String lang = s.getRegion().getLangCode();
            if(language.equalsIgnoreCase(lang)) {
                site = s;
            }else if("zh".equalsIgnoreCase(lang)) {
                chSite = s;
            }
        }
        if(site == null) {
            return chSite;
        }
        return site;
    }

    private Set<Site> getSitesThroughDomain(String domain) throws Exception{
        Host host = hostService.getHost(domain);
        if(host == null) {
            throw new Exception("域名错误");
        }
        return host.getSites();
    }


    private boolean isRootPath(String path) {
        return "/".equals(path);
    }

    private boolean isSubSitePath(String path) {
        return path.lastIndexOf("/") == 0;
    }

}
