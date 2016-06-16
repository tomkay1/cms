package com.huotu.hotcms.web.Filter;

import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.SiteConfig;
import com.huotu.hotcms.service.repository.SiteConfigRepository;
import com.huotu.hotcms.service.service.SiteConfigService;
import com.huotu.hotcms.service.service.impl.SiteConfigServiceImpl;
import com.huotu.hotcms.service.thymeleaf.service.RouteResolverService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.util.CheckMobile;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 解析过滤器
 * </p>
 *
 * @author xhl
 * @since 1.0.0
 */
public class RouteFilter implements Filter {
    private static final Log log = LogFactory.getLog(RouteFilter.class);
    private static final String filter = "interim/join";
    private static final String manage = "/manage/";
    private static final String[] diy_filter = new String[]{"/shop", "/bind", "/template/0/", "/template/error/"
            , ".js", ".css"};//DIY网站过滤规则->(PC官网装修,PC商城装修)
    private ApplicationContext applicationContext;
    private ServletContext servletContext;

    private SiteResolveService siteResolveService;
    private RouteResolverService routeResolverService;
    private SiteConfigServiceImpl siteConfigService;

    private static boolean isChecked=false;//拦截规则是否检测过

    private boolean isContains(String servletPath) {
        for (String str : diy_filter) {
            if (servletPath.contains(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("RouteFilter Init.");
        servletContext = filterConfig.getServletContext();
    }

    /**
     * DIY网站过滤拦截方法,个性化装修(官网装修、商城装修)
     */
    private boolean personaliseFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws Exception {
        HttpServletRequest request1 = ((HttpServletRequest) request);
//        SiteResolveService siteResolveService = (SiteResolveService) applicationContext.getBean("siteResolveService");
//        Site site = siteResolveService.getCurrentSite(request1);
//        String servletPath = PatternMatchUtil.getServletPath(site, request1);
        String servletPath = request1.getServletPath();
        if (!isContains(servletPath)) {
            if (servletPath.equals("/")) {
                boolean isMobile = CheckMobile.check(request1);
                if (isMobile) {
                    Site site = siteResolveService.getCurrentSite(request1);
                    String mobileUrl=siteConfigService.findMobileUrlBySite(site);
                    if(mobileUrl!=null&&mobileUrl!=""){//开启了手机微官网则重定向微官网域名地址
                        ((HttpServletResponse) response).sendRedirect(mobileUrl);
                        return false;
                    }
                }
                request.getRequestDispatcher("/shop" + servletPath).forward(request, response);
                return  false;
            }
        }
        return true;
    }

    /**
     * 定制网站过滤拦截方法
     */
    private boolean customizeFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws Exception {
        isChecked=true;
        HttpServletRequest request1 = ((HttpServletRequest) request);

        Site site = siteResolveService.getCurrentSite(request1);
        //目前为了兼容我们公司自己的官网，暂时先这样处理兼容,后面考虑在网站配置中新增一个字段(是否有手机官网，如果有则做该业务判断),统一使用m.xxx.com为手机官网地址
        if (site.getCustomerId().equals(5)) {
            boolean isMobile = CheckMobile.check(request1);
            if (isMobile) {
                String mobileUrl = CheckMobile.getMobileUrl(request1);
                ((HttpServletResponse) response).sendRedirect(mobileUrl);
                return false;
            }
        }

        String servletPath = PatternMatchUtil.getServletPath(site, request1);//获得ServletPath 国际化带语言参数经一步处理(移除国际化参数信息)得到的跟配置的路由一致
        String langParam = PatternMatchUtil.getEffecterLangParam(request1, site);//获得国际化参数(url上带上的语言地区参数信息)
        if (!servletPath.contains(filter)) {
            if (PatternMatchUtil.isMatchFilter(servletPath)) {
                Route route = routeResolverService.getRoute(site, servletPath);
                if(!isStaticResc(request1.getServletPath())){
                    if (route == null && !site.isCustom()) {
                        request.getRequestDispatcher("/template/" + site.getCustomerId() + servletPath).forward(request
                                , response);
                    } else {
                        if (!StringUtils.isEmpty(langParam)) {//语言参数不为空追加上语言参数并做服务端forward
                            request.getRequestDispatcher("/web/" + langParam + servletPath).forward(request, response);
                        } else {
                            request.getRequestDispatcher("/web" + servletPath).forward(request, response);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException
            , ServletException {
        try {
            if (applicationContext == null) {
                String key = FrameworkServlet.SERVLET_CONTEXT_PREFIX
                        + AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
                applicationContext = (ApplicationContext) servletContext.getAttribute(key);
                if (applicationContext == null) {
                    //在测试环境中 servlet 的名字是空的
                    applicationContext = (ApplicationContext) servletContext
                            .getAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX);
                }
                if (applicationContext == null)
                    throw new ServletException("Spring ApplicationContext Required.");
            }
            if (siteResolveService == null) {
                siteResolveService = applicationContext.getBean("siteResolveService", SiteResolveService.class);
            }
            if (routeResolverService == null) {
                routeResolverService = applicationContext.getBean("routeResolverService", RouteResolverService.class);
            }
            if(siteConfigService==null){
                siteConfigService = applicationContext.getBean("siteConfigServiceImpl", SiteConfigServiceImpl.class);
            }
            HttpServletRequest request1 = ((HttpServletRequest) request);
            Site site = siteResolveService.getCurrentSite(request1);

            if(!((HttpServletRequest) request).getServletPath().contains(manage)){
                boolean Flag = site.isPersonalise() ? personaliseFilter(request, response, chain) :
                        customizeFilter(request, response, chain);
                if (!Flag)
                    return;
            }

        } catch (Exception ex) {
            log.error("doFilter", ex);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean isStaticResc(String path){
        String staticRescSuffix[]={".js",".css",".png",".jpg",".jpeg",".gif"};
        for(String s:staticRescSuffix){
            if(path.endsWith(s)){
                return true;
            }
        }
        return false;
    }
}
