/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.SiteConfig;
import com.huotu.hotcms.service.repository.SiteConfigRepository;
import com.huotu.hotcms.service.service.SiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/4/29.
 */
@Service
@Component
public class SiteConfigServiceImpl implements SiteConfigService {

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    @Override
    public SiteConfig findBySite(Site site) {
       return siteConfigRepository.findBySite(site);
    }

    @Override
    public String findMobileUrlBySite(Site site) {
        SiteConfig siteConfig=findBySite(site);
        if (siteConfig != null && siteConfig.isEnabledMobileSite()) {
            return "http://"+siteConfig.getMobileDomain();
        }
        return null;
    }
}
