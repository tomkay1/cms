package com.huotu.hotcms.web.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
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
public class SiteResolveService {

    @Autowired
    private HostService hostService;

    @Autowired
    private RegionService regionService;

    public Site getCurrentSite(HttpServletRequest request) throws Exception{
        Site site = new Site();
        String path = request.getServletPath();
        String domain = request.getServerName();
        Set<Site> sites = getSitesThroughDomain(domain);
        String language = "";
        if(isHomeSitePath(path)) {
            language = request.getLocale().getLanguage();
            if(StringUtils.isEmpty(language)) {
                language = "zh";
            }
        }else {
            String regionCode = "";
            if(path.substring(1).contains("/")) {
                regionCode = path.substring(0, path.indexOf("/", 0));
            }else {
                regionCode = path.substring(1);
            }
            Region region = regionService.getRegion(regionCode);
            if(region == null) {
                throw new Exception("请求错误");
            }
            language = region.getLangCode();
        }
        for(Site s : sites) {
            String lang = s.getRegion().getLangCode();
            if(language.equalsIgnoreCase(lang)) {
                site = s;
                break;
            }
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

    public boolean isHomeSitePath(String path) {
        path = path.substring(1);
        if("".equals(path)) {
            return true;
        }
        if(path.contains("/")) {
            path = path.substring(0,path.indexOf("/",0));
            return isRegionCode(path);
        }
        return isRegionCode(path);
    }

    public boolean isRegionCode(String str) {
        if(!str.matches("\\D[2]")) {
            return true;
        }
        return false;
    }

}
