/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author CJ
 */
public abstract class AbstractSiteSupperController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private HostService hostService;
    @Autowired
    private ResourceService resourceService;

    protected void someThing(@RequestParam(value = "id", defaultValue = "0") Long id, ModelAndView modelAndView, String logo_uri) throws IOException {
        if (id != 0) {
            Site site = siteService.getSite(id);
            if (site != null) {
                if (!StringUtils.isEmpty(site.getLogoUri())) {
                    logo_uri = resourceService.getResource(site.getLogoUri()).httpUrl().toString();
                }
                modelAndView.addObject("site", site);
                modelAndView.addObject("logo_uri", logo_uri);
                String domains = "";
                for (Host host : hostService.hookOn(site)) {
                    String domain = host.getDomain();
                    domains = domains + domain + ",";
                }
                Region region = site.getRegion();
                modelAndView.addObject("siteTypes", SiteType.values());
                modelAndView.addObject("region", region);
                modelAndView.addObject("homeDomain", hostService.getHomeDomain(site));
                modelAndView.addObject("domains", domains.substring(0, domains.length() - 1));
            }
        }
    }
}
