/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.admin.service;

import com.huotu.hotcms.entity.Host;
import com.huotu.hotcms.entity.Region;
import com.huotu.hotcms.entity.Site;
import com.huotu.hotcms.repository.HostRepository;
import com.huotu.hotcms.repository.RegionRepository;
import com.huotu.hotcms.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cwb on 2015/12/28.
 */
@Service
public class InitService {

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private SiteRepository siteRepository;

    @PostConstruct
    @Transactional
    public void initDatabase() {
        if(regionRepository.count() == 0) {
            Region region = new Region();
            region.setLangCode("zh");
            region.setLangName("中文");
            region.setRegionCode("cn");
            region.setRegionName("大陆");
            region.setLangTag("zh-cn");
            regionRepository.save(region);
            Region region1 = new Region();
            region1.setLangCode("en");
            region1.setLangName("英文");
            region1.setRegionCode("us");
            region1.setRegionName("美国");
            region1.setLangTag("en-us");
            regionRepository.save(region1);
        }
        if(siteRepository.count() == 0) {
            Host host = new Host();
            host.setDomain("cms.51flashmall.com");
            Host host1 = new Host();
            host1.setDomain("localhost");
            Site site = new Site();
            site.setCustomerId(3447);
            site.setRegion(regionRepository.findByRegionCodeIgnoreCase("cn"));
            site.setCreateTime(LocalDateTime.now());
            site.addHost(host);
            site.addHost(host1);
            siteRepository.save(site);
            /*Host host = new Host();
            host.setDomain("cms.51flashmall.com");
            Host host1 = new Host();
            host1.setDomain("localhost");
            host = hostRepository.save(host);
            host1 = hostRepository.save(host1);
            Site site = new Site();
            site.setCustomerId(3447);
            site.setRegion(regionRepository.findByRegionCodeIgnoreCase("cn"));
            site.setCreateTime(LocalDateTime.now());
            site = siteRepository.save(site);
            site.addHost(host);
            site.addHost(host1);
            site = siteRepository.save(site);
            host.addSite(site);
            host1.addSite(site);
            hostRepository.save(host);
            hostRepository.save(host1);*/
        }
    }
}
