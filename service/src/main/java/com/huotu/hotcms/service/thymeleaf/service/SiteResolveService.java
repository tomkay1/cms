/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service;

import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.RegionService;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log log = LogFactory.getLog(SiteResolveService.class);

    private static final String default_country="cn";

    private static final String default_language="zh";

    @Autowired
    private HostService hostService;

    @Autowired
    private RegionService regionService;

    /**
     * 根据当前浏览器环境获得站点
     * */
    public Site getEnvironmentSite(HttpServletRequest request) throws Exception{
        Site site = null;
        try {
            String domain = request.getServerName();
            Set<Site> sites = getSitesThroughDomain(domain);
            String language = request.getLocale().getLanguage();
            String country = request.getLocale().getCountry();
            Region region = regionService.getRegionByLangCodeAndRegionCode(language, country);
            if (region != null) {
                for (Site s : sites) {
                    String lang = s.getRegion().getLangCode();
                    String regionCode = s.getRegion().getRegionCode();
                    if (language.equalsIgnoreCase(lang) && country.equalsIgnoreCase(regionCode)) {
                        site = s;
                        break;
                    }
                }
            } else {
                for (Site s : sites) {
                    String lang = s.getRegion().getLangCode();
                    if (language.equalsIgnoreCase(lang)) {
                        site = s;
                        break;
                    }
                }
            }
        }catch (Exception ex){
            throw new Exception("getEnvironmentSite exception->"+ex.getMessage()+" domain-->"+request.getServerName());
        }
        return site;
    }

    /**
     * 根据当前地址栏中输入的言语参数来获得站点信息
     * */
    public Site getParamSite(HttpServletRequest request,String langParam) throws Exception{
        String domain = request.getServerName();
        Set<Site> sites = getSitesThroughDomain(domain);
        if(langParam.contains("-")){//完整的语言地区参数
            for(Site s : sites) {
                String regionCode1=s.getRegion().getRegionCode();
                String langCode1=s.getRegion().getLangCode();
                if(langParam.equalsIgnoreCase(langCode1+"-"+regionCode1)) {
                    return s;
                }
            }
        }else{//地区参数
            for(Site s : sites) {
                String regionCode=s.getRegion().getRegionCode();
                if(langParam.equalsIgnoreCase(regionCode)) {
                    return s;
                }
            }
        }
       return null;
    }

    public Site getCurrentSite(HttpServletRequest request) throws Exception{
//        String languageParam= PatternMatchUtil.getUrlString(path,PatternMatchUtil.langRegexp);
        Site site=null;
        String languageParam=PatternMatchUtil.getLangParam(request);
        if(StringUtils.isEmpty(languageParam)){
            site= getEnvironmentSite(request);
        }else{
            if(languageParam.contains("-")){
                if(regionService.isRegionByCode(languageParam.split("-")[1])){
                    site= getParamSite(request,languageParam);
                }else{
                    site= getEnvironmentSite(request);
                }
            }else{
                if(regionService.isRegionByCode(languageParam)){
                    site= getParamSite(request,languageParam);
                }else{
                    site= getEnvironmentSite(request);
                }
            }
        }
        if(site==null){//其他语言站点没有则默认访问中文站点
            String domain = request.getServerName();
            site=getSiteByDomainAndLange(domain,default_language);
            if(site!=null) {
                String language = request.getLocale().getLanguage();
                String country = request.getLocale().getCountry();
            }
        }
        return site;
//        if(isHomeSitePath(path)) {
//            language = request.getLocale().getLanguage();
//            if(StringUtils.isEmpty(language)) {
//                language = "zh";
//            }
//        }else {
//            String regionCode = "";
//            if(path.substring(1).contains("/")) {
//                regionCode = path.substring(0, path.indexOf("/", 0));
//            }else {
//                regionCode = path.substring(1);
//            }
//            Region region = regionService.getRegion(regionCode);
//            if(region == null) {
//                throw new Exception("请求错误");
//            }
//            language = region.getLangCode();
//        }
//        for(Site s : sites) {
//            String lang = s.getRegion().getLangCode();
//
//            if(language.equalsIgnoreCase(lang)) {
//                site = s;
//                break;
//            }
//        }
//        return site;
    }

    public Set<Site> getSitesThroughDomain(String domain) throws Exception{
        Host host = hostService.getHost(domain);
        if(host == null) {
            throw new Exception("domain no find");
        }
        return host.getSites();
    }

    /**
     * 根据域名以及语言来获得唯一的站点信息
     * **/
    public Site getSiteByDomainAndLange(String domain,String language) throws Exception{
        Site site=null;
        Set<Site> sites=getSitesThroughDomain(domain);
        for (Site s : sites) {
            String lang = s.getRegion().getLangCode();
            if (language.equalsIgnoreCase(lang)) {
                site = s;
                break;
            }
        }
        return site;
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
