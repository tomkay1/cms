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
            region.setRegionCode("CN");
            region.setRegionName("中国大陆");
            region.setLangTag("zh-CN");
            regionRepository.save(region);
            Region region1 = new Region();
            region1.setLangCode("en");
            region1.setLangName("英文");
            region1.setRegionCode("US");
            region1.setRegionName("美国");
            region1.setLangTag("en-US");
            regionRepository.save(region1);
            Region region2 = new Region();
            region2.setLangCode("ko");
            region2.setLangName("韩文");
            region2.setRegionCode("KR");
            region2.setRegionName("韩国");
            region2.setLangTag("ko-KR");
            regionRepository.save(region2);
        }
        if(siteRepository.count() == 0) {
            Host host = new Host();
            host.setDomain("cms.51flashmall.com");
            host.setCustomerId(3447);
            Host host1 = new Host();
            host1.setDomain("localhost");
            host1.setCustomerId(3447);
            Site site = new Site();
            site.setCustomerId(3447);
            site.setTitle("火图科技");
            site.setDescription("杭州火图科技有限公司是一家专业的微信商城服务提供商，" +
                    "专业/质优/高端微商城定制开发，为客户建立专属微信三级分销系统，并提供代运营、微商人才培训等服务");
            site.setRegion(regionRepository.findByRegionCodeIgnoreCase("cn"));
            site.setCreateTime(LocalDateTime.now());
            site.addHost(host);
            site.addHost(host1);
            siteRepository.save(site);
            Site s = new Site();
            s.setCustomerId(3447);
            s.setTitle("huobanplus");
            s.setDescription("Hangzhou fire science and Technology Co., Ltd. is a professional mall of micro channel service providers, " +
                    "professional / quality / high-end micro mall custom development……");
            s.setRegion(regionRepository.findByRegionCodeIgnoreCase("us"));
            s.setCreateTime(LocalDateTime.now());
            s = siteRepository.save(s);
            s.addHost(hostRepository.findByDomain("cms.51flashmall.com"));
            siteRepository.save(s);
        }
    }
}
