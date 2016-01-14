package com.huotu.hotcms.web.Filter;

import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.service.RouteResolverService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *     解析过滤器过滤器
 * </p>
 *
 * @author xhl
 *
 * @since 1.0.0
 *
 */
public class RouteFilter implements Filter {
    private static final Log log = LogFactory.getLog(RouteFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest request1 = ((HttpServletRequest) request);
            String servletPath = request1.getServletPath();
            if(PatternMatchUtil.isMatchFilter(servletPath)) {
                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
                SiteResolveService siteResolveService = (SiteResolveService) applicationContext.getBean("siteResolveService");
                RouteResolverService routeResolverService = (RouteResolverService) applicationContext.getBean("routeResolverService");
                Site site = siteResolveService.getCurrentSite(request1);
                if (site != null) {
                    Route route = routeResolverService.getRoute(site, servletPath);
                    if (route == null) {
                        request.getRequestDispatcher("/template/" + site.getCustomerId() + servletPath).forward(request, response);
                    } else {
                        request.getRequestDispatcher("/web"+servletPath).forward(request, response);
                    }
                    return;
                } else {
                    chain.doFilter(request,response);
                }
            }
//            int status=((HttpServletResponse) response).getStatus();
//            if(status==404){
//                request.getRequestDispatcher("/web/404").forward(request, response);
//            }
//            return;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
