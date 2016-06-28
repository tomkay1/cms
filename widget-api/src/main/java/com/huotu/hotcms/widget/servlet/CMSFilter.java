/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.servlet;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.exception.NoHostFoundException;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.widget.CMSContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个Filer所完成的工作就是让{@link CMSContext#RequestContext()}可用
 * 服务器需要将CMSFilter拉入Servlet环境中.
 *
 * @author CJ
 */
public class CMSFilter extends OncePerRequestFilter implements Filter {

    private static final Log log = LogFactory.getLog(CMSFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        WebApplicationContext context = WebApplicationContextUtils.findWebApplicationContext(getServletContext());

        try {
            Site site = context.getBean(SiteResolveService.class).getCurrentSite(request);
            CMSContext.PutContext(request, response, site);
        } catch (NoSiteFoundException | NoHostFoundException e) {
            log.info("Redirect http://www.huobanplus.com ", e);
            response.sendRedirect("http://www.huobanplus.com");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
