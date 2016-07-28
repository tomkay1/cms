/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.CMSDataVersion;
import com.huotu.hotcms.service.common.SiteType;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import me.jiangcai.lib.upgrade.VersionUpgrade;
import me.jiangcai.lib.upgrade.service.UpgradeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 初始化服务
 *
 * @author CJ
 */
@Service
public class InitService {

    private static final Log log = LogFactory.getLog(InitService.class);

    @Autowired
    private UpgradeService upgradeService;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private Environment environment;
    @Autowired
    private SiteService siteService;

    @Transactional
    @PostConstruct
    public void init() {

        // 系统升级
        upgradeService.systemUpgrade(new VersionUpgrade<CMSDataVersion>() {
            @Override
            public void upgradeToVersion(CMSDataVersion version) throws Exception {
                log.debug(" to version:" + version);
            }
        });

        if (environment.acceptsProfiles("test")) {
            // 应当构造 localhost host for localhost site
            Owner owner = ownerRepository.findByCustomerId(3447);
            if (owner == null) {
                owner = new Owner();
                owner.setCustomerId(3447);
                owner.setEnabled(true);
                owner = ownerRepository.save(owner);
            }
            Host host = hostRepository.findByDomain("localhost");
            if (host == null || siteRepository.count() == 0) {
                Site site = new Site();
                site.setOwner(owner);
                site.setCreateTime(LocalDateTime.now());
                site.setName("本地站点");
                site.setTitle("标题是什么");
                site.setSiteType(SiteType.SITE_PC_WEBSITE);

                siteService.newSite(new String[]{"localhost", "mycms.51flashmall.com"}, "localhost", site, Locale.CHINA);

                host = hostRepository.findByDomain("localhost");
                host.setRemarks("本地开发所用的host");
                host = hostRepository.save(host);
            }

        }
    }

}
