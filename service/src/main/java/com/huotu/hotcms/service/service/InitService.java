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
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.exception.NoSiteFoundException;
import com.huotu.hotcms.service.repository.HostRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import me.jiangcai.lib.jdbc.ConnectionConsumer;
import me.jiangcai.lib.jdbc.ConnectionProvider;
import me.jiangcai.lib.jdbc.JdbcService;
import me.jiangcai.lib.upgrade.VersionUpgrade;
import me.jiangcai.lib.upgrade.service.UpgradeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.sql.Statement;
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
    private ConfigService configService;
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
    @Autowired
    private JdbcService jdbcService;

    @Transactional
    @PostConstruct
    public void init() throws NoSiteFoundException {

        // 系统升级
        upgradeService.systemUpgrade(new VersionUpgrade<CMSDataVersion>() {
            @Override
            public void upgradeToVersion(CMSDataVersion version) throws Exception {
                log.debug(" to version:" + version);
                switch (version) {
                    case version101000:
                        jdbcService.runStandaloneJdbcWork(new ConnectionConsumer() {
                            @Override
                            public void accept(ConnectionProvider connection) throws SQLException {
                                try (Statement statement = connection.getConnection().createStatement()) {
                                    statement.execute("ALTER TABLE cms_link ADD linkType int(11) null");
//                                    statement.execute("ALTER TABLE cms_link ADD linkType int(11) null");
                                }
                            }
                        });
//                        jdbcService.tableAlterAddColumn(Link.class,"linkType",null);
                        jdbcService.tableAlterAddColumn(Link.class, "pageInfoID", null);
                        break;
                    case siteRecommendDomain:
                        jdbcService.tableAlterAddColumn(Site.class, "recommendDomain", null);
                        break;
                }
            }
        });

//        if (environment.acceptsProfiles("test")) {
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
//            site.setSiteType(SiteType.SITE_PC_WEBSITE);

            siteService.newSite(new String[]{"localhost", "cms." + configService.getMallDomain()}, "localhost", site
                    , Locale.CHINA);

            host = hostRepository.findByDomain("localhost");
            host.setRemarks("本地开发所用的host");
            host = hostRepository.save(host);
        }

        host = hostRepository.findByDomain("cms." + configService.getMallDomain());
        if (host == null) {
            // 先找站点
            Site site = siteService.closestSite(hostRepository.findByDomain("localhost"), Locale.CHINA);

            siteService.patchSite(new String[]{"localhost", "cms." + configService.getMallDomain()}, "localhost"
                    , site, Locale.CHINA);
        }

//        }
    }

}
