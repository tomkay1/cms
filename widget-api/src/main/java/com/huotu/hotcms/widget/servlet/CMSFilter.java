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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 这个Filer所完成的工作就是让{@link CMSContext#RequestContext()}可用
 * 服务器需要将CMSFilter拉入Servlet环境中.
 *
 * @author CJ
 */
public class CMSFilter extends OncePerRequestFilter implements Filter {

    private static final Log log = LogFactory.getLog(CMSFilter.class);

    /**
     * 这个构造是Servlet容器发起的
     */
    public CMSFilter() {
    }

    /**
     * 在测试的情况下，我们无法调用{@link #init(FilterConfig)}所有需要额外构造参数
     * @param context context
     */
    public CMSFilter(ServletContext context){
        super();
        setServletContext(context);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        WebApplicationContext context = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        try {
            // Locale 在Spring MVC中 是在DispatchServlet内才开始解析的, 在我们个案中到了Spring就晚了,所以得自己动手
            // 至少在这个一步我们已经获得Host了, 也考虑更改设计为先获取Host再根据Host的设置获取不同的区域处理器
            Locale locale = context.getBean("localeResolver", LocaleResolver.class).resolveLocale(request);
            // 这个值是否正确 还需要再研究哈
            Site site = context.getBean(SiteResolveService.class).getCurrentSite(request, locale);
            CMSContext.PutContext(request, response, site);
        } catch (NoSiteFoundException | NoHostFoundException e) {
            log.info("Redirect http://www.huobanplus.com ", e);
            response.sendRedirect("http://www.huobanplus.com");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
