package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.util.Validate;

import java.util.Set;


/**
 * Created by cwb on 2015/12/31.
 */
public class SiteWebService {

    private final IWebContext context;
    @Autowired
    private HostService hostService;

    public SiteWebService(final IWebContext context) {
        super();
        Validate.notNull(context,"Context cannot be null");
        this.context = context;
    }


    public Set<Site> siteList() throws Exception{
        String domain = context.getRequest().getServerName();
        return getSitesThroughDomain(domain);
    }

    private Set<Site> getSitesThroughDomain(String domain) throws Exception{
        Host host = hostService.getHost(domain);
        if(host == null) {
            throw new Exception("域名错误");
        }
        return host.getSites();
    }
}
