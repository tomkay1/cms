package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * <p>
 *     站点解析服务
 * </p>
 * @author xhl
 *
 * @since 1.0.0
 */
@Component
public class SiteResolveServcice {

    @Autowired
    private HostService hostService;

    @Autowired
    private RegionService regionService;

    public Site getHomeSite(HttpServletRequest request) throws Exception{
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

    public Set<Site> getSitesThroughDomain(String domain) throws Exception{
        Host host = hostService.getHost(domain);
        if(host == null) {
            throw new Exception("域名错误");
        }
        return host.getSites();
    }
}
