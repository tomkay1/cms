/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.interceptor;

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

/**
 * Created by cwb on 2015/12/21.
 */
@Component
public class ManageSiteResolver implements HandlerMethodArgumentResolver {


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
        return initSiteParameter(request);
    }

    private Site initSiteParameter(HttpServletRequest request) throws Exception {
        Site site = new Site();
        boolean initSuccess = false;
        String siteId = request.getParameter("siteId");
        if(!StringUtils.isEmpty(siteId)) {
            site = siteService.getSite(Long.parseLong(request.getParameter("siteId")));
            if(site == null) {
                throw new Exception("站点不存在");
            }
            initSuccess = true;
        }
        if (!StringUtils.isEmpty(request.getParameter("ownerId"))) {
            // what??
//            site.setCustomerId(Integer.parseInt(request.getParameter("ownerId")));
            initSuccess = true;
        }
        if(!StringUtils.isEmpty(request.getParameter("logoUri"))) {
            site.setLogoUri(request.getParameter("logoUri"));
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
        if(!StringUtils.isEmpty(request.getParameter("resourceUrl"))) {
            site.setResourceUrl(request.getParameter("resourceUrl"));
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
