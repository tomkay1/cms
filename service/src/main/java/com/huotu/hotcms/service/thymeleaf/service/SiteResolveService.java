/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.NoHostFoundException;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * <p>
 * 站点解析服务
 * </p>
 *
 * @author xhl
 * @since 1.0.0
 */
@Component
public class SiteResolveService {
    private static final Log log = LogFactory.getLog(SiteResolveService.class);
    @Autowired
    private HostService hostService;

    @Autowired
    private SiteService siteService;

    /**
     * 根据当前请求的语言
     *
     * @param request
     * @return 语言方位
     */
    public Locale localeFromRequest(HttpServletRequest request) {
        // TODO
        // String languageParam = PatternMatchUtil.getLangParam(request);
        return request.getLocale();
    }

    /**
     * 获取当前站点
     *
     * @param request servlet 请求
     * @return 站点
     * @throws NoSiteFoundException 找不到站点
     * @throws NoHostFoundException 找不到主机
     */
    public Site getCurrentSite(HttpServletRequest request) throws NoSiteFoundException, NoHostFoundException {
        String domain = request.getServerName();
        Host host = hostService.getHost(domain);
        if (host == null) {
            throw new NoHostFoundException(domain);
        }

        Locale locale = localeFromRequest(request);

        //接下来是寻找Site
        return siteService.closestSite(host, locale);
    }

}
